package tech.corefinance.userprofile.common.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.dto.LoginDto;
import tech.corefinance.common.dto.UserRoleDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.AppVersion;
import tech.corefinance.userprofile.common.entity.CommonRole;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;
import tech.corefinance.userprofile.common.entity_author.CommonLoginSession;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public interface UserAuthenAddOn<U extends CommonUserProfile<?>, T extends CommonLoginSession<?>> extends Ordered {

    default int getOrder() {
        return 0;
    }

    default JwtTokenDto updateTokenDto(JwtTokenDto jwtTokenDto, String deviceId, String clientAppId, AppPlatform appPlatform,
                                       AppVersion appVersion, HttpServletRequest request, T commonLoginSession, U userProfile,
                                       List<UserRoleDto> userRoles, String account, String password, Map<String, Object> additionalInfo) {
        jwtTokenDto.setUserDisplayName(userProfile.getDisplayName());
        jwtTokenDto.setUsername(userProfile.getUsername());
        jwtTokenDto.setUserEmail(userProfile.getEmail());
        jwtTokenDto.setUserRoles(userRoles);
        var dataMap = new HashMap<String, Serializable>();
        if (userProfile.getAdditionalAttributes() != null) {
            Map<String, Object> additionalAttributes = userProfile.getAdditionalAttributes();
            for (Map.Entry<String, Object> entry : additionalAttributes.entrySet()) {
                var value = entry.getValue();
                if (value instanceof Serializable serVal) {
                    dataMap.put(entry.getKey(), serVal);
                }
            }
        }
        jwtTokenDto.setAdditionalInfo(dataMap);
        Set<String> tenantIdSet = userRoles.stream().map(UserRoleDto::getResourceId).collect(Collectors.toSet());
        if (tenantIdSet.size() == 1) {
            jwtTokenDto.setTenantId(tenantIdSet.iterator().next());
        } else {
            jwtTokenDto.setTenantId(null);
        }
        return jwtTokenDto;
    }

    default LoginDto updateLoginDto(LoginDto loginDto, U userProfile, T commonLoginSession, List<UserRoleDto> userRoles,
                                    String jwtToken, String refreshToken, Map<String, Object> additionalInfo, JwtTokenDto jwtTokenDto) {
        loginDto.setLoginId(commonLoginSession.getId());
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
        loginDto.setFirstName(userProfile.getFirstName());
        loginDto.setLastName(userProfile.getLastName());
        loginDto.setAdditionalInfo(additionalInfo);
        loginDto.setExpiredIn(jwtTokenDto.getExpiredIn());
        loginDto.setExpiredAt(jwtTokenDto.getExpiredAt());
        return loginDto;
    }

    default List<UserRoleDto> buildUserRoleDtoList(U userProfile, Map<String, Object> additionalInfo) {
        var log = LoggerFactory.getLogger(getClass());
        List<? extends CommonRole<?>> commonRoles = userProfile.getCommonRoles();

        if (CollectionUtils.isEmpty(commonRoles)) {
            log.warn("User id {} don't have any roles", userProfile.getId());
            return new ArrayList<>();
        }

        List<UserRoleDto> result = new LinkedList<>();
        for (CommonRole<?> commonRole : commonRoles) {
            UserRoleDto userRoleDto = new UserRoleDto();
            userRoleDto.setRoleId(commonRole.getId());
            userRoleDto.setResourceType("ORGANIZATION");
            userRoleDto.setResourceId(commonRole.getTenantId());
            userRoleDto.setRoleName(commonRole.getName());
            result.add(userRoleDto);
        }
        return result;
    }

    /**
     * Check authentication for provided userprofile, username, password
     *
     * @param account        Username or email
     * @param password       Password
     * @param additionalInfo Additional login info
     * @return Modified Userprofile
     */
    U authenticate(U userProfile, String account, String password, Map<String, Object> additionalInfo);

    /**
     * Refresh login token
     *
     * @param userProfile    User
     * @param additionalInfo Additional authen data
     * @return Modified Userprofile
     */
    default U refreshAuthen(U userProfile, Map<String, Object> additionalInfo, CommonLoginSession<?> loginSession) {
        userProfile.getAdditionalAttributes().put("token-type", "refresh");
        LoggerFactory.getLogger(getClass()).debug("Additional info [{}] for login session [{}]", additionalInfo, loginSession.getId());
        return userProfile;
    }

    default Map<String, Object> retrieveAdditionalLoginInfo(Map<String, Object> additionalLoginInfo, HttpServletRequest request) {
        return new HashMap<>();
    }
}

