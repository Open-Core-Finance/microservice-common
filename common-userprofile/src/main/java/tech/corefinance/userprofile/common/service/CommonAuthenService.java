package tech.corefinance.userprofile.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import tech.corefinance.common.dto.LoginDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.AppVersion;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;
import tech.corefinance.userprofile.common.entity_author.CommonLoginSession;

import java.net.UnknownHostException;

public interface CommonAuthenService<U extends CommonUserProfile<?>, T extends CommonLoginSession<U>> {

    LoginDto login(String username, String password, String deviceId, String clientAppId, AppPlatform appPlatform, AppVersion appVersion,
                   HttpServletRequest request) throws Exception;

    void unlockUser(String email);

    LoginDto refreshToken(String loginId, String refreshToken, String deviceId, String clientAppId, AppPlatform appPlatform,
                          AppVersion appVersion, HttpServletRequest request) throws UnknownHostException, JsonProcessingException;

    boolean isValidToken(String loginId);

    void invalidateLogin(String loginId);
}
