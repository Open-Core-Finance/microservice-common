package tech.corefinance.userprofile.common.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.config.JwtConfiguration;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.dto.LoginDto;
import tech.corefinance.common.dto.UserRoleDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.ex.ResourceNotFound;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.model.AppVersion;
import tech.corefinance.common.service.JwtService;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;
import tech.corefinance.userprofile.common.entity_author.AttemptedLogin;
import tech.corefinance.userprofile.common.entity_author.CommonLoginSession;
import tech.corefinance.userprofile.common.repository.AttemptedLoginRepository;
import tech.corefinance.userprofile.common.repository.CommonLoginSessionRepository;
import tech.corefinance.userprofile.common.service.CommonAuthenService;
import tech.corefinance.userprofile.common.service.UserAuthenAddOn;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@ConditionalOnProperty(prefix = "tech.corefinance.app.userprofile", name = "common-role-service", havingValue = "true",
        matchIfMissing = true)
public abstract class AbstractAuthenService<U extends CommonUserProfile<?>, T extends CommonLoginSession<U>>
        implements CommonAuthenService<U, T> {
    @Autowired
    private CommonLoginSessionRepository<T> loginSessionRepository;
    @Autowired
    private AttemptedLoginRepository attemptedLoginRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtConfiguration jwtConfiguration;

    @SuppressWarnings({"rawtypes", "MismatchedQueryAndUpdateOfCollection"})
    @Autowired
    private List<UserAuthenAddOn> userAuthenAddOns;

    @SuppressWarnings("unchecked")
    private UserAuthenAddOn<U> getSuitableUserAuthenAddOn() {
        return this.userAuthenAddOns.getFirst();
    }

    public abstract T createEmptySession();

    protected abstract int invalidateOldLogins(String verifyKey);

    private LoginDto createToken(U userProfile, String deviceId, String clientAppId, AppPlatform appPlatform, AppVersion appVersion,
                                 HttpServletRequest request, String account, String password, Map<String, Object> additionalInfo)
            throws JsonProcessingException, UnknownHostException {
        var userAuthenAddOn = getSuitableUserAuthenAddOn();
        T loginSession = loginSessionRepository.save(createEmptySession());
        List<UserRoleDto> userRoles = userAuthenAddOn.buildUserRoleDtoList(userProfile, additionalInfo);

        JwtTokenDto jwtTokenDto =
                userAuthenAddOn.buildJwtTokenDto(deviceId, clientAppId, appPlatform, appVersion, request, loginSession, userProfile,
                        userRoles, account, password, additionalInfo);

        String jwtToken = jwtService.buildLoginToken(jwtTokenDto);
        String refreshToken = jwtService.buildRefreshToken(jwtTokenDto, jwtToken);

        // Update login session
        loginSession.setId(jwtTokenDto.getLoginId());
        loginSession.setLoginToken(jwtToken);
        loginSession.setRefreshToken(refreshToken);
        loginSession.setValidToken(true);
        loginSession.setUserProfile(userProfile);
        loginSession.setVerifyKey(jwtTokenDto.getVerifyKey());
        loginSession.setAdditionalInfo(additionalInfo);
        loginSession.setInputAccount(account);
        loginSession.setInputPassword(password);
        if (jwtTokenDto.getAdditionalInfo() != null) {
            var dataMap = loginSession.getAdditionalInfo();
            if (dataMap == null) {
                loginSession.setAdditionalInfo(new HashMap<>(jwtTokenDto.getAdditionalInfo()));
            } else {
                dataMap.putAll(jwtTokenDto.getAdditionalInfo());
            }
        }
        var invalidatedCount = invalidateOldLogins(loginSession.getVerifyKey());
        log.debug("Invalidated {} sessions", invalidatedCount);
        loginSession = loginSessionRepository.save(loginSession);

        // Login DTO
        return userAuthenAddOn.buildLoginDto(userProfile, loginSession, userRoles, jwtToken, refreshToken, additionalInfo);
    }

    @Override
    @Transactional(noRollbackFor = ServiceProcessingException.class)
    public LoginDto login(String account, String password, String deviceId, String clientAppId, AppPlatform appPlatform,
                          AppVersion appVersion, HttpServletRequest request) throws Exception {
        long loginCount = attemptedLoginRepository.countByAccountAndEnabled(account, true);
        if (loginCount >= jwtConfiguration.getMaxLoginFailAllowed()) {
            throw new ServiceProcessingException(GeneralApiResponse.createErrorResponseWithCode("user_locked"));
        }
        var userAuthenAddOn = getSuitableUserAuthenAddOn();
        var additionalInfo = userAuthenAddOn.retrieveAdditionalLoginInfo(request);
        U userProfile = userAuthenAddOn.authenticate(account, password, additionalInfo);
        if (userProfile == null) {
            // Store attempted login
            attemptedLoginRepository.save(
                    new AttemptedLogin(account, jwtService.extractIpAddress(request), request.getHeader("user-agent"), clientAppId,
                            deviceId, appPlatform, appVersion, additionalInfo));
            throw new ServiceProcessingException(GeneralApiResponse.createErrorResponseWithCode("login_fail"));
        }
        // Clean attempted login
        attemptedLoginRepository.updateEnabledByAccount(false, userProfile.getUsername(), userProfile.getEmail());
        // Response token
        return createToken(userProfile, deviceId, clientAppId, appPlatform, appVersion, request, account, password, additionalInfo);
    }

    @Override
    public void unlockUser(String account) {
        attemptedLoginRepository.updateEnabledByAccount(false, account, account);
    }

    @Override
    public LoginDto refreshToken(String loginId, String refreshToken, String deviceId, String clientAppId, AppPlatform appPlatform,
                                 AppVersion appVersion, HttpServletRequest request) throws UnknownHostException, JsonProcessingException {
        T loginSession = loginSessionRepository.findByIdAndRefreshToken(loginId, refreshToken)
                .orElseThrow(() -> new AccessDeniedException("ID or refresh token is wrong"));

        validateLoginSession(deviceId, request, loginSession);

        loginSession.setValidToken(false);
        loginSessionRepository.save(loginSession);

        return createToken(loginSession.getUserProfile(), deviceId, clientAppId, appPlatform, appVersion, request,
                loginSession.getInputAccount(), loginSession.getInputPassword(), loginSession.getAdditionalInfo());
    }

    private void validateLoginSession(String deviceId, HttpServletRequest request, T loginSession) throws UnknownHostException {
        try {
            jwtService.verify(loginSession.getLoginToken(), deviceId, jwtService.extractIpAddress(request));
        } catch (JWTVerificationException e) {
            throw new AccessDeniedException("IP Address is changed or Device ID is not correct");
        }
    }

    @Override
    public boolean isValidToken(String loginId) {
        T loginSession = loginSessionRepository.findById(loginId).orElseThrow(() -> new ResourceNotFound("Login session not found"));
        return loginSession.isValidToken();
    }

    @Override
    public void invalidateLogin(String loginId) {
        T loginSession = loginSessionRepository.findById(loginId).orElseThrow(() -> new ResourceNotFound("Login session not found"));
        loginSession.setValidToken(false);
        loginSessionRepository.save(loginSession);
    }
}
