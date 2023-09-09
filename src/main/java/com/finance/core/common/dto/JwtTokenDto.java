package com.finance.core.common.dto;

import com.finance.core.common.enums.AppPlatform;
import com.finance.core.common.model.AppVersion;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

/**
 * @author Trung Doan
 */
@Data
@AllArgsConstructor
public class JwtTokenDto {

    private String loginId;

    private String userId;

    private String username;

    private String userEmail;

    private long expiredIn;

    private String deviceId;

    private String clientAppId;
    private AppPlatform appPlatform;
    private AppVersion appVersion;

    /**
     * This key to used as unique value to store to any storage like: cache, DB,... <br/>
     * Unique value is mean: <br/>
     * - if we found existed valid value store, we will reject the new login.
     * - if we found existed in-valid value store, we will reject the request using this jwt.
     */
    private String verifyKey;

    private String loginIpAddr;

    private String userDisplayName;

    /**
     * To identify this jwt is valid or not. <br/>
     * Please refer {@link #verifyKey}
     */
    private boolean valid = true;

    private String originalToken;

    private Collection<UserRoleDto> userRoles;

    public JwtTokenDto() {
        this.loginId = UUID.randomUUID().toString();
    }

    public JwtTokenDto(String loginId, String userId, String clientAppId, AppPlatform appPlatform,
                       AppVersion appVersion, String deviceId, String loginIpAddr) {
        this.loginId = loginId;
        this.userId = userId;
        this.clientAppId = clientAppId;
        this.appPlatform = appPlatform;
        this.appVersion = appVersion;
        this.deviceId = deviceId;
        this.loginIpAddr = loginIpAddr;
        this.userRoles = new LinkedList<>();
    }
}
