package tech.corefinance.userprofile.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import tech.corefinance.common.dto.LoginDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.AppVersion;

import java.net.UnknownHostException;

public interface AuthenService {

    LoginDto login(String username, String password, String deviceId,
                   String clientAppId, AppPlatform appPlatform, AppVersion appVersion,
                   HttpServletRequest request) throws Exception;

    void unlockUser(String email);

    LoginDto refreshToken(String loginId, String refreshToken, String deviceId,
                          String clientAppId, AppPlatform appPlatform, AppVersion appVersion,
                          HttpServletRequest request) throws UnknownHostException, JsonProcessingException;

    boolean isValidToken(String loginId);

    void invalidateLogin(String loginId);
}
