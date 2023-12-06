package tech.corefinance.userprofile.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import tech.corefinance.userprofile.entity.Role;
import tech.corefinance.userprofile.entity.UserProfile;
import tech.corefinance.userprofile.repository.AttemptedLoginRepository;
import tech.corefinance.userprofile.repository.LoginSessionRepository;
import tech.corefinance.userprofile.repository.UserProfileRepository;
import tech.corefinance.userprofile.service.AuthenService;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenServiceImpl implements AuthenService {

    private final LoginSessionRepository loginSessionRepository;
    private final AttemptedLoginRepository attemptedLoginRepository;
    private final JwtService jwtService;
    private final JwtConfiguration jwtConfiguration;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    private LoginDto createToken(UserProfile userProfile, String deviceId, String clientAppId,
                                 AppPlatform appPlatform, AppVersion appVersion, HttpServletRequest request
    ) throws JsonProcessingException, UnknownHostException {
        LoginSession loginSession = loginSessionRepository.save(new LoginSession());
        List<UserRoleDto> userRoles = buildUserRoleDtoList(userProfile);

        JwtTokenDto jwtTokenDto =
                buildJwtTokenDto(deviceId, clientAppId, appPlatform, appVersion, request, loginSession, userProfile,
                        userRoles);

        String jwtToken = jwtService.buildLoginToken(jwtTokenDto);
        String refreshToken = jwtService.buildRefreshToken(jwtTokenDto, jwtToken);

        // Update login session
        loginSession.setId(jwtTokenDto.getLoginId());
        loginSession.setLoginToken(jwtToken);
        loginSession.setRefreshToken(refreshToken);
        loginSession.setValidToken(true);
        loginSession.setUserProfile(userProfile);
        loginSession.setVerifyKey(jwtTokenDto.getVerifyKey());
        loginSessionRepository.invalidateOldLogins(loginSession.getVerifyKey());
        loginSession = loginSessionRepository.save(loginSession);

        // Login DTO
        return buildLoginDto(userProfile, loginSession, userRoles, jwtToken, refreshToken);
    }

    private JwtTokenDto buildJwtTokenDto(String deviceId, String clientAppId, AppPlatform appPlatform,
                                         AppVersion appVersion, HttpServletRequest request, LoginSession loginSession,
                                         UserProfile userProfile, List<UserRoleDto> userRoles)
            throws UnknownHostException {
        JwtTokenDto jwtTokenDto = new JwtTokenDto(loginSession.getId(), userProfile.getId(),
                clientAppId, appPlatform, appVersion, deviceId, jwtService.extractIpAddress(request));
        jwtTokenDto.setUserDisplayName(userProfile.getLastName() + " " + userProfile.getFirstName());
        jwtTokenDto.setUsername(userProfile.getUsername());
        jwtTokenDto.setUserEmail(userProfile.getEmail());
        jwtTokenDto.setUserRoles(userRoles);
        Set<String> tenantIdSet = userRoles.stream().map(UserRoleDto::getResourceId).collect(Collectors.toSet());
        if (tenantIdSet.size() == 1) {
            jwtTokenDto.setTenantId(tenantIdSet.iterator().next());
        } else {
            jwtTokenDto.setTenantId(null);
        }
        return jwtTokenDto;
    }

    private LoginDto buildLoginDto(UserProfile userProfile, LoginSession loginSession,
                                   List<UserRoleDto> userRoles, String jwtToken,
                                   String refreshToken) {
        LoginDto loginDto = new LoginDto();
        loginDto.setLoginId(loginSession.getId());
        loginDto.setToken(jwtToken);
        loginDto.setRefreshToken(refreshToken);
        loginDto.setUserRoles(userRoles);
        loginDto.setUserId(userProfile.getId());
        loginDto.setDisplayName(userProfile.getDisplayName());
        loginDto.setUsername(userProfile.getUsername());
        loginDto.setUserEmail(userProfile.getEmail());
        loginDto.setGender(userProfile.getGender());
        loginDto.setAddress(userProfile.getAddress());
        loginDto.setBirthday(userProfile.getBirthday());
        loginDto.setPhoneNumber(userProfile.getPhoneNumber());
        loginDto.setFistName(userProfile.getFirstName());
        loginDto.setLastName(userProfile.getLastName());
        return loginDto;
    }

    private List<UserRoleDto> buildUserRoleDtoList(UserProfile userProfile) {
        List<Role> roles = userProfile.getRoles();

        if (CollectionUtils.isEmpty(roles)) {
            log.warn("userProfile id: " + userProfile.getId() + " don't have any roles");
            return new ArrayList<>();
        }

        return roles.stream().map(this::fromUserProfileRole)
                .collect(Collectors.toList());
    }

    private UserRoleDto fromUserProfileRole(Role role) {
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDto.setRoleId(role.getId());
        userRoleDto.setResourceType("ORGANIZATION");
        userRoleDto.setResourceId(role.getTenantId());
        return userRoleDto;
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
        UserProfile userProfile = authenticate(account, password);
        if (userProfile == null) {
            // Store attempted login
            attemptedLoginRepository.save(
                    new AttemptedLogin(account, jwtService.extractIpAddress(request), request.getHeader("user-agent"),
                            clientAppId, deviceId, appPlatform, appVersion));
            throw new ServiceProcessingException(GeneralApiResponse.createErrorResponseWithCode("login_fail"));
        }
        // Clean attempted login
        attemptedLoginRepository.updateEnabledByAccount(false, userProfile.getUsername(), userProfile.getEmail());
        // Response token
        return createToken(userProfile, deviceId, clientAppId, appPlatform, appVersion, request);
    }

    private UserProfile authenticate(String account, String password) {
        Optional<UserProfile> optional =
                userProfileRepository.findFirstByEmailIgnoreCaseOrUsernameIgnoreCase(account, account);
        if (optional.isPresent()) {
            UserProfile userprofile = optional.get();
            log.debug("Comparing input password [{}] and user password [{}]", password, userprofile.getPassword());
            boolean result = passwordEncoder.matches(password, userprofile.getPassword());
            log.debug("Comparing result [{}]", result);
            if (result) {
                log.debug("Return user [{}]", userprofile);
                return userprofile;
            }
        }
        log.debug("Return null for user [{}]!!", account);
        return null;
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

        return createToken(loginSession.getUserProfile(), deviceId, clientAppId, appPlatform, appVersion, request);
    }

    private void validateLoginSession(String deviceId, HttpServletRequest request, LoginSession loginSession)
            throws UnknownHostException {
        if (!loginSession.isValidToken()) {
            throw new AccessDeniedException("The refresh token is invalid");
        }
        try {
            jwtService.verfiy(loginSession.getLoginToken(), deviceId, jwtService.extractIpAddress(request));
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
