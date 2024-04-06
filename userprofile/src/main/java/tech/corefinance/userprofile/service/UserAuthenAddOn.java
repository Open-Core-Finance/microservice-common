package tech.corefinance.userprofile.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.dto.LoginDto;
import tech.corefinance.common.dto.UserRoleDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.AppVersion;
import tech.corefinance.common.service.JwtService;
import tech.corefinance.userprofile.entity.LoginSession;
import tech.corefinance.userprofile.entity.Role;
import tech.corefinance.userprofile.entity.UserProfile;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

public interface UserAuthenAddOn extends Ordered {

    JwtService getJwtService();

    default JwtTokenDto buildJwtTokenDto(String deviceId, String clientAppId, AppPlatform appPlatform, AppVersion appVersion,
                                         HttpServletRequest request, LoginSession loginSession, UserProfile userProfile,
                                         List<UserRoleDto> userRoles,  String account, String password, Map<String, Object> additionalInfo)
            throws UnknownHostException {
        JwtTokenDto jwtTokenDto = new JwtTokenDto(loginSession.getId(), userProfile.getId(),
                clientAppId, appPlatform, appVersion, deviceId, getJwtService().extractIpAddress(request));
        jwtTokenDto.setUserDisplayName(userProfile.getDisplayName());
        jwtTokenDto.setUsername(userProfile.getUsername());
        jwtTokenDto.setUserEmail(userProfile.getEmail());
        jwtTokenDto.setUserRoles(userRoles);
        var dataMap = new HashMap<String, Serializable>();
        if (userProfile.getAdditionalAttributes() != null) {
            for (Map.Entry<String, Object> entry: userProfile.getAdditionalAttributes().entrySet()) {
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

    default LoginDto buildLoginDto(UserProfile userProfile, LoginSession loginSession,
                                   List<UserRoleDto> userRoles, String jwtToken,
                                   String refreshToken, Map<String, Object> additionalInfo) {
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
        loginDto.setFirstName(userProfile.getFirstName());
        loginDto.setLastName(userProfile.getLastName());
        loginDto.setAdditionalInfo(additionalInfo);
        return loginDto;
    }

    default List<UserRoleDto> buildUserRoleDtoList(UserProfile userProfile, Map<String, Object> additionalInfo) {
        var log = LoggerFactory.getLogger(getClass());
        List<Role> roles = userProfile.getRoles();

        if (CollectionUtils.isEmpty(roles)) {
            log.warn("User id {} don't have any roles", userProfile.getId());
            return new ArrayList<>();
        }

        return roles.stream().map((Role role) -> {
            UserRoleDto userRoleDto = new UserRoleDto();
            userRoleDto.setRoleId(role.getId());
            userRoleDto.setResourceType("ORGANIZATION");
            userRoleDto.setResourceId(role.getTenantId());
            userRoleDto.setRoleName(role.getName());
            return userRoleDto;
        }).collect(Collectors.toList());
    }

    UserProfile authenticate(String account, String password, Map<String, Object> additionalInfo);

    default Map<String, Object> retrieveAdditionalLoginInfo(HttpServletRequest request) {
        return new HashMap<>();
    }
}
