package tech.corefinance.userprofile.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import tech.corefinance.userprofile.entity.AttemptedLogin;
import tech.corefinance.userprofile.entity.LoginSession;
import tech.corefinance.userprofile.entity.UserProfile;
import tech.corefinance.userprofile.repository.AttemptedLoginRepository;
import tech.corefinance.userprofile.repository.LoginSessionRepository;
import tech.corefinance.userprofile.service.AuthenService;
import tech.corefinance.userprofile.service.UserAuthenAddOn;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenServiceImpl implements AuthenService {

    private final LoginSessionRepository loginSessionRepository;
    private final AttemptedLoginRepository attemptedLoginRepository;
    private final JwtService jwtService;
    private final JwtConfiguration jwtConfiguration;
    private final List<UserAuthenAddOn> userAuthenAddOns;

    private UserAuthenAddOn getSuitableUserAuthenAddOn() {
        return this.userAuthenAddOns.iterator().next();
    }

    private LoginDto createToken(UserProfile userProfile, String deviceId, String clientAppId,
                                 AppPlatform appPlatform, AppVersion appVersion, HttpServletRequest request,
                                 String account, String password, Map<String, Object> additionalInfo
    ) throws JsonProcessingException, UnknownHostException {
        var userAuthenAddOn = getSuitableUserAuthenAddOn();
        LoginSession loginSession = loginSessionRepository.save(new LoginSession());
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
        loginSessionRepository.invalidateOldLogins(loginSession.getVerifyKey());
        loginSession = loginSessionRepository.save(loginSession);

        // Login DTO
        return userAuthenAddOn.buildLoginDto(userProfile, loginSession, userRoles, jwtToken, refreshToken, additionalInfo);
    }

    @Override
    @Transactional(noRollbackFor = ServiceProcessingException.class)
    public LoginDto login(String account, String password, String deviceId,
                          String clientAppId, AppPlatform appPlatform, AppVersion appVersion,
                          HttpServletRequest request) throws Exception {
        long loginCount = attemptedLoginRepository.countByAccountAndEnabled(account, true);
        if (loginCount >= jwtConfiguration.getMaxLoginFailAllowed()) {
            throw new ServiceProcessingException(GeneralApiResponse.createErrorResponseWithCode("user_locked"));
        }
        var userAuthenAddOn = getSuitableUserAuthenAddOn();
        var additionalInfo = userAuthenAddOn.retrieveAdditionalLoginInfo(request);
        UserProfile userProfile = userAuthenAddOn.authenticate(account, password, additionalInfo);
        if (userProfile == null) {
            // Store attempted login
            attemptedLoginRepository.save(
                    new AttemptedLogin(account, jwtService.extractIpAddress(request), request.getHeader("user-agent"),
                            clientAppId, deviceId, appPlatform, appVersion, additionalInfo));
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
    public LoginDto refreshToken(String loginId, String refreshToken, String deviceId, String clientAppId,
                                 AppPlatform appPlatform, AppVersion appVersion, HttpServletRequest request)
            throws UnknownHostException, JsonProcessingException {
        LoginSession loginSession = loginSessionRepository.findByIdAndRefreshToken(loginId, refreshToken)
                .orElseThrow(() -> new AccessDeniedException("ID or refresh token is wrong"));

        validateLoginSession(deviceId, request, loginSession);

        loginSession.setValidToken(false);
        loginSessionRepository.save(loginSession);

        return createToken(loginSession.getUserProfile(), deviceId, clientAppId, appPlatform, appVersion, request,
                loginSession.getInputAccount(), loginSession.getInputPassword(), loginSession.getAdditionalInfo());
    }

    private void validateLoginSession(String deviceId, HttpServletRequest request, LoginSession loginSession)
            throws UnknownHostException {
        try {
            jwtService.verify(loginSession.getLoginToken(), deviceId, jwtService.extractIpAddress(request));
        } catch (JWTVerificationException e) {
            throw new AccessDeniedException("IP Address is changed or Device ID is not correct");
        }
    }

    @Override
    public boolean isValidToken(String loginId) {
        LoginSession loginSession = loginSessionRepository.findById(loginId)
                .orElseThrow(() -> new ResourceNotFound("Login session not found"));
        return loginSession.isValidToken();
    }

    @Override
    public void invalidateLogin(String loginId) {
        LoginSession loginSession = loginSessionRepository.findById(loginId)
                .orElseThrow(() -> new ResourceNotFound("Login session not found"));
        loginSession.setValidToken(false);
        loginSessionRepository.save(loginSession);
    }
}
