package tech.corefinance.userprofile.common.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import tech.corefinance.userprofile.common.entity.CommonRole;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;
import tech.corefinance.userprofile.common.entity_author.AttemptedLogin;
import tech.corefinance.userprofile.common.entity_author.CommonLoginSession;
import tech.corefinance.userprofile.common.repository.AttemptedLoginRepository;
import tech.corefinance.userprofile.common.repository.CommonLoginSessionRepository;
import tech.corefinance.userprofile.common.repository.CommonUserProfileRepository;
import tech.corefinance.userprofile.common.service.CommonAuthenService;
import tech.corefinance.userprofile.common.service.UserAuthenAddOn;

import java.net.UnknownHostException;
import java.util.*;

@Slf4j
@Service
@Transactional
public abstract class AbstractAuthenService<R extends CommonRole<?>, U extends CommonUserProfile<?>, T extends CommonLoginSession<U>>
        implements CommonAuthenService {
    @Autowired
    protected CommonLoginSessionRepository<T> loginSessionRepository;
    @Autowired
    protected AttemptedLoginRepository attemptedLoginRepository;
    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected JwtConfiguration jwtConfiguration;
    @SuppressWarnings("rawtypes")
    @Autowired
    protected CommonUserProfileRepository commonUserProfileRepository;

    @SuppressWarnings({"rawtypes", "MismatchedQueryAndUpdateOfCollection"})
    @Autowired
    protected List<UserAuthenAddOn> userAuthenAddOns;

    @PostConstruct
    public void postConstruct() {
        this.userAuthenAddOns.sort(Comparator.comparingInt(UserAuthenAddOn::getOrder));
    }

    public abstract T createEmptySession();

    protected abstract int invalidateOldLogins(String verifyKey);

    @SuppressWarnings("unchecked")
    @Transactional
    private LoginDto createToken(U userProfile, String deviceId, String clientAppId, AppPlatform appPlatform, AppVersion appVersion,
            HttpServletRequest request, String account, String password, Map<String, Object> additionalInfo) throws UnknownHostException {
        T loginSession = loginSessionRepository.save(createEmptySession());
        if (log.isDebugEnabled()) {
            log.debug("Created new login session!");
        }
        List<UserRoleDto> userRoles = new LinkedList<>();
        for (UserAuthenAddOn<?, ?> userAuthenAddOn : userAuthenAddOns) {
            var tmp = ((UserAuthenAddOn<U, T>) userAuthenAddOn);
            if (log.isDebugEnabled()) {
                log.debug("Loading role by [{}]...", tmp.getClass().getName());
            }
            userRoles.addAll(tmp.buildUserRoleDtoList(userProfile, additionalInfo));
        }

        JwtTokenDto jwtTokenDto = new JwtTokenDto(loginSession.getId(), userProfile.getId(), clientAppId, appPlatform, appVersion, deviceId,
                                                  jwtService.extractIpAddress(request));
        for (UserAuthenAddOn<?, ?> userAuthenAddOn : userAuthenAddOns) {
            var tmp = ((UserAuthenAddOn<U, T>) userAuthenAddOn);
            if (log.isDebugEnabled()) {
                log.debug("Updating login info to login token by [{}]...", tmp.getClass().getName());
            }
            jwtTokenDto =
                    tmp.updateTokenDto(jwtTokenDto, deviceId, clientAppId, appPlatform, appVersion, request, loginSession, userProfile,
                                       userRoles, account, password, additionalInfo);
        }

        if (log.isDebugEnabled()) {
            log.debug("Generation JWT Token...");
        }
        String jwtToken = jwtService.buildLoginToken(jwtTokenDto);
        if (log.isDebugEnabled()) {
            log.debug("Generation JWT refresh Token...");
        }
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
        if (log.isDebugEnabled()) {
            log.debug("Invalidating old login session...");
        }
        var invalidatedCount = invalidateOldLogins(loginSession.getVerifyKey());
        if (log.isDebugEnabled()) {
            log.debug("Invalidated {} sessions", invalidatedCount);
            log.debug("Saving new session into database...");
        }
        loginSession = loginSessionRepository.save(loginSession);

        // Login DTO
        LoginDto loginDto = new LoginDto();
        if (log.isDebugEnabled()) {
            log.debug("Building login response DTO....");
        }
        for (UserAuthenAddOn<?, ?> userAuthenAddOn : userAuthenAddOns) {
            var tmp = ((UserAuthenAddOn<U, T>) userAuthenAddOn);
            if (log.isDebugEnabled()) {
                log.debug("[{}] Update login response DTO....", tmp.getClass().getName());
            }
            loginDto =
                    tmp.updateLoginDto(loginDto, userProfile, loginSession, userRoles, jwtToken, refreshToken, additionalInfo, jwtTokenDto);
        }
        if (log.isDebugEnabled()) {
            log.debug("Login finished!");
        }
        return loginDto;
    }

    @Override
    @Transactional(noRollbackFor = ServiceProcessingException.class)
    @SuppressWarnings("unchecked")
    public LoginDto login(String account, String password, String deviceId, String clientAppId, AppPlatform appPlatform,
            AppVersion appVersion, HttpServletRequest request) throws Exception {
        // Check if login
        long loginCount = attemptedLoginRepository.countByAccountAndEnabled(account, true);
        if (loginCount >= jwtConfiguration.getMaxLoginFailAllowed()) {
            if (log.isErrorEnabled()) {
                log.error(
                        "Number of login fail [{}] over max login allowed [{}]! Account temporary locked! Please contact admin to unlock!",
                        loginCount, jwtConfiguration.getMaxLoginFailAllowed());
            }
            throw new ServiceProcessingException(GeneralApiResponse.createErrorResponseWithCode("user_locked"));
        }

        // Additional login data
        Map<String, Object> additionalInfo = new HashMap<>();
        for (UserAuthenAddOn<?, ?> userAuthenAddOn : userAuthenAddOns) {
            var tmp = ((UserAuthenAddOn<U, T>) userAuthenAddOn);
            if (log.isDebugEnabled()) {
                log.debug("Retrieving additional info from request [{}]", tmp.getClass().getName());
            }
            additionalInfo.putAll(tmp.retrieveAdditionalLoginInfo(additionalInfo, request));
        }

        //Lookup user from database
        Optional<?> optional = commonUserProfileRepository.findFirstByEmailIgnoreCaseOrUsernameIgnoreCase(account, account);
        if (log.isDebugEnabled()) {
            log.debug("Retrieving userprofile from database for account [{}]...", account);
        }
        if (optional.isEmpty()) {
            if (log.isErrorEnabled()) {
                log.error("Cannot find userprofile in database! Recorded as login fail!");
            }
            // Store attempted login
            attemptedLoginRepository.save(
                    new AttemptedLogin(account, jwtService.extractIpAddress(request), request.getHeader("user-agent"), clientAppId,
                                       deviceId, appPlatform, appVersion, additionalInfo));
            throw new ServiceProcessingException(GeneralApiResponse.createErrorResponseWithCode("login_fail"));
        }

        U userProfile = (U) optional.get();
        for (UserAuthenAddOn<?, ?> userAuthenAddOn : userAuthenAddOns) {
            var tmp = ((UserAuthenAddOn<U, T>) userAuthenAddOn);
            if (log.isDebugEnabled()) {
                log.debug("Authenticating with [{}]...", tmp.getClass().getName());
            }
            userProfile = tmp.authenticate(userProfile, account, password, additionalInfo);
            if (userProfile == null) {
                if (log.isErrorEnabled()) {
                    log.debug("Authenticating with [{}] fail!", tmp.getClass().getName());
                }
                throw new ServiceProcessingException(GeneralApiResponse.createErrorResponseWithCode("login_fail"));
            }
            if (log.isDebugEnabled()) {
                log.debug("Authenticating with [{}] successfully!", tmp.getClass().getName());
            }
        }
        // Clean attempted login
        if (log.isDebugEnabled()) {
            log.debug("Trying to clear old fail attempted login...");
        }
        attemptedLoginRepository.updateEnabledByAccount(false, userProfile.getUsername(), userProfile.getEmail());
        // Response token
        if (log.isDebugEnabled()) {
            log.debug("Creating login token...");
        }
        return createToken(userProfile, deviceId, clientAppId, appPlatform, appVersion, request, account, password, additionalInfo);
    }

    @Override
    @Transactional
    public void unlockUser(String account) {
        if (log.isDebugEnabled()) {
            log.debug("Unlocking user [{}]...", account);
        }
        attemptedLoginRepository.updateEnabledByAccount(false, account, account);
    }

    @Override
    @Transactional
    public LoginDto refreshToken(String loginId, String refreshToken, String deviceId, String clientAppId, AppPlatform appPlatform,
            AppVersion appVersion, HttpServletRequest request) throws UnknownHostException {
        if (log.isDebugEnabled()) {
            log.debug("Attempting to refresh login token for login ID {}", loginId);
        }
        if (log.isTraceEnabled()) {
            log.trace("Refresh token {}", refreshToken);
        }
        T loginSession = loginSessionRepository.findByIdAndRefreshToken(loginId, refreshToken)
                .orElseThrow(() -> new AccessDeniedException("ID or refresh token is wrong"));
        if (log.isTraceEnabled()) {
            log.trace("Validating session...");
        }
        validateLoginSession(deviceId, request, loginSession);

        if (log.isTraceEnabled()) {
            log.trace("Invalid old login session");
        }
        loginSession.setValidToken(false);
        loginSessionRepository.save(loginSession);
        if (log.isDebugEnabled()) {
            log.debug("Saved new login session [{}]!", loginSession.getId());
        }

        U userProfile = loginSession.getUserProfile();
        for (UserAuthenAddOn<?, ?> userAuthenAddOn : userAuthenAddOns) {
            @SuppressWarnings("unchecked") var tmp = ((UserAuthenAddOn<U, T>) userAuthenAddOn);
            if (log.isDebugEnabled()) {
                log.debug("Refreshing authen by [{}]...!", tmp.getClass().getName());
            }
            userProfile = tmp.refreshAuthen(userProfile, loginSession.getAdditionalInfo(), loginSession);
            if (userProfile == null) {
                if (log.isErrorEnabled()) {
                    log.error("Refreshing authen by [{}] fail!", tmp.getClass().getName());
                }
                throw new ServiceProcessingException(GeneralApiResponse.createErrorResponseWithCode("login_fail"));
            }
        }

        // Clean attempted login
        if (log.isDebugEnabled()) {
            log.debug("Clean up old fail attempted login...");
        }
        attemptedLoginRepository.updateEnabledByAccount(false, userProfile.getUsername(), userProfile.getEmail());

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
    @Transactional
    public void invalidateLogin(String loginId) {
        T loginSession = loginSessionRepository.findById(loginId).orElseThrow(() -> new ResourceNotFound("Login session not found"));
        loginSession.setValidToken(false);
        loginSessionRepository.save(loginSession);
    }
}
