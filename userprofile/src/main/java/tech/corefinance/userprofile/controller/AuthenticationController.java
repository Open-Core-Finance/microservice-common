package tech.corefinance.userprofile.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.dto.LoginDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.AppVersion;
import tech.corefinance.userprofile.dto.RefreshTokenRequestDto;
import tech.corefinance.userprofile.service.AuthenService;

import static tech.corefinance.common.enums.CommonConstants.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/authentication")
@ControllerManagedResource("authen")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenService authenService;

    @PostMapping(value = "/login")
    public GeneralApiResponse<LoginDto> login(
            @RequestHeader(name = HEADER_KEY_CLIENT_APP_ID, defaultValue = DEFAULT_CLIENT_APP_ID) String clientAppId,
            @RequestHeader(name = HEADER_KEY_APP_PLATFORM, defaultValue = DEFAULT_APP_PLATFORM_STRING)
            AppPlatform appPlatform,
            @RequestHeader(name = HEADER_KEY_APP_VERSION, defaultValue = DEFAULT_VERSION_JSON) AppVersion appVersion,
            @RequestHeader(DEVICE_ID) String deviceId, HttpServletRequest request,
            @RequestParam("username") String username, @RequestParam("password") String password)
            throws Exception {
        LoginDto dto = authenService.login(username, password, deviceId, clientAppId, appPlatform, appVersion, request);
        return GeneralApiResponse.createSuccessResponse(dto);
    }

    @PostMapping(value = "/unlock-user")
    public GeneralApiResponse<Boolean> unlockUser(@RequestParam("account") String account) {
        authenService.unlockUser(account);
        return GeneralApiResponse.createSuccessResponse(true);
    }

    @PostMapping(value = "/refresh-token")
    public GeneralApiResponse<LoginDto> refreshToken(
            @RequestHeader(name = HEADER_KEY_CLIENT_APP_ID, defaultValue = DEFAULT_CLIENT_APP_ID) String clientAppId,
            @RequestHeader(name = HEADER_KEY_APP_PLATFORM, defaultValue = DEFAULT_APP_PLATFORM_STRING)
            AppPlatform appPlatform,
            @RequestHeader(name = HEADER_KEY_APP_VERSION, defaultValue = DEFAULT_VERSION_JSON) AppVersion appVersion,
            @RequestHeader(DEVICE_ID) String deviceId, HttpServletRequest request,
            RefreshTokenRequestDto refreshTokenRequestDto) throws Exception {
        LoginDto dto = authenService.refreshToken(refreshTokenRequestDto.getLoginId(),
                refreshTokenRequestDto.getRefreshToken(), deviceId,
                clientAppId, appPlatform, appVersion, request);
        return GeneralApiResponse.createSuccessResponse(dto);
    }

    @GetMapping(value = "/login-session/{loginId}/valid")
    public GeneralApiResponse<Boolean> isValidToken(@PathVariable String loginId) {
        return GeneralApiResponse.createSuccessResponse(authenService.isValidToken(loginId));
    }

    @PostMapping(value = "/login-session/{loginId}/invalidate")
    public GeneralApiResponse<Boolean> invalidateLogin(@PathVariable String loginId) {
        authenService.invalidateLogin(loginId);
        return GeneralApiResponse.createSuccessResponse(true);
    }
}
