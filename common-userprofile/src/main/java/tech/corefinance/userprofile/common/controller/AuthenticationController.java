package tech.corefinance.userprofile.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.dto.LoginDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.AppVersion;
import tech.corefinance.userprofile.common.dto.RefreshTokenRequestDto;
import tech.corefinance.userprofile.common.service.CommonAuthenService;

import static tech.corefinance.common.enums.CommonConstants.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/authentication")
@ControllerManagedResource("authen")
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
@RequiredArgsConstructor
public class AuthenticationController {

    private final CommonAuthenService commonAuthenService;

    @PostMapping(value = "/login")
    @PermissionAction(action = "login")
    public GeneralApiResponse<LoginDto> login(
            @RequestHeader(name = HEADER_KEY_CLIENT_APP_ID, defaultValue = DEFAULT_CLIENT_APP_ID) String clientAppId,
            @RequestHeader(name = HEADER_KEY_APP_PLATFORM, defaultValue = DEFAULT_APP_PLATFORM_STRING) AppPlatform appPlatform,
            @RequestHeader(name = HEADER_KEY_APP_VERSION, defaultValue = DEFAULT_VERSION_JSON) AppVersion appVersion,
            @RequestHeader(name = DEVICE_ID, required = false) String deviceId, HttpServletRequest request,
            @RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        LoginDto dto = commonAuthenService.login(username, password, deviceId, clientAppId, appPlatform, appVersion, request);
        return GeneralApiResponse.createSuccessResponse(dto);
    }

    @PostMapping(value = "/unlock-user")
    @PermissionAction(action = "update")
    public GeneralApiResponse<Boolean> unlockUser(@RequestParam("account") String account) {
        commonAuthenService.unlockUser(account);
        return GeneralApiResponse.createSuccessResponse(true);
    }

    @PostMapping(value = "/refresh-token")
    @PermissionAction(action = "login")
    public GeneralApiResponse<LoginDto> refreshToken(
            @RequestHeader(name = HEADER_KEY_CLIENT_APP_ID, defaultValue = DEFAULT_CLIENT_APP_ID) String clientAppId,
            @RequestHeader(name = HEADER_KEY_APP_PLATFORM, defaultValue = DEFAULT_APP_PLATFORM_STRING) AppPlatform appPlatform,
            @RequestHeader(name = HEADER_KEY_APP_VERSION, defaultValue = DEFAULT_VERSION_JSON) AppVersion appVersion,
            @RequestHeader(name = DEVICE_ID, required = false) String deviceId, HttpServletRequest request,
            RefreshTokenRequestDto refreshTokenRequestDto) throws Exception {
        LoginDto dto =
                commonAuthenService.refreshToken(refreshTokenRequestDto.getLoginId(), refreshTokenRequestDto.getRefreshToken(), deviceId,
                        clientAppId, appPlatform, appVersion, request);
        return GeneralApiResponse.createSuccessResponse(dto);
    }

    @GetMapping(value = "/login-session/{loginId}/valid")
    @PermissionAction(action = "check")
    public GeneralApiResponse<Boolean> isValidToken(@PathVariable String loginId) {
        return GeneralApiResponse.createSuccessResponse(commonAuthenService.isValidToken(loginId));
    }

    @PostMapping(value = "/login-session/{loginId}/invalidate")
    @PermissionAction(action = "update")
    public GeneralApiResponse<Boolean> invalidateLogin(@PathVariable String loginId) {
        commonAuthenService.invalidateLogin(loginId);
        return GeneralApiResponse.createSuccessResponse(true);
    }
}
