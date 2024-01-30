package tech.corefinance.common.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.corefinance.common.config.JwtConfiguration;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.enums.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Map;

public interface JwtService {

    /**
     * Signing data.
     *
     * @param data Data to sign.
     * @return JWT token
     */
    String sign(Map<String, Serializable> data);

    /**
     * Verify JWT token.
     *
     * @param token     JWT token
     * @param deviceId  Login device ID
     * @param ipaddress Client IP Address
     * @return True if token active
     */
    DecodedJWT verify(String token, String deviceId, String ipaddress);

    /**
     * Extract client IP Address from request.
     *
     * @param httpServletRequest Request
     * @return Client IP Address
     * @throws UnknownHostException When cannot read ip address
     */
    String extractIpAddress(HttpServletRequest httpServletRequest) throws UnknownHostException;

    /**
     * Retreive JWT data from request.
     *
     * @param httpServletRequest  HttpServletRequest
     * @param httpServletResponse HttpServletResponse
     * @return JwtTokenDto Object
     * @throws IOException When read fail
     */
    Map<String, JwtTokenDto> retrieveTokenFromRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;
    /**
     * Build JWT token for login info
     * @param jwtTokenDto Token DTO
     * @return JWT token string
     */
    default String buildLoginToken(JwtTokenDto jwtTokenDto) throws JsonProcessingException {
        Logger logger = LoggerFactory.getLogger(getClass());
        jwtTokenDto.setVerifyKey(buildLoginVerifyKey(jwtTokenDto));
        logger.debug("Built verify key [{}]", jwtTokenDto.getVerifyKey());
        return sign(buildLoginDataMap(jwtTokenDto));
    }

    JwtConfiguration getJwtConfiguration();

    default String buildLoginVerifyKey(JwtTokenDto jwtTokenDto) {
        Logger log = LoggerFactory.getLogger(getClass());
        String loginMode = getJwtConfiguration().getLoginMode();
        log.debug("Login mode [{}]", loginMode);
        return switch (loginMode) {
            case CommonConstants.JWT_VERIFY_MODE_SINGLE_LOGIN -> jwtTokenDto.getUserId();
            case CommonConstants.JWT_VERIFY_MODE_SINGLE_LOGIN_PER_APP ->
                    jwtTokenDto.getUserId() + "||APP||" + jwtTokenDto.getClientAppId();
            case CommonConstants.JWT_VERIFY_MODE_SINGLE_LOGIN_PER_DEVICE ->
                    jwtTokenDto.getUserId() + "||DEVICE||" + jwtTokenDto.getDeviceId();
            default -> jwtTokenDto.getLoginId();
        };
    }

    Map<String, Serializable> buildLoginDataMap(JwtTokenDto jwtTokenDto) throws JsonProcessingException;

    /**
     * Build JWT refresh token for login info
     * @param jwtTokenDto Token DTO
     * @param token JWT token string
     * @return JWT refresh token string
     */
    default String buildRefreshToken(JwtTokenDto jwtTokenDto, String token) throws JsonProcessingException {
        return sign(buildRefreshTokenDataMap(jwtTokenDto, token));
    }

    Map<String, Serializable> buildRefreshTokenDataMap(JwtTokenDto jwtTokenDto, String token)
            throws JsonProcessingException;

    /**
     * Retrieve user info from login token.
     *
     * @param jwtTokenDto Login token.
     *
     * @return User from token
     */
    BasicUserDto retrieveUserAsAttribute(JwtTokenDto jwtTokenDto);
}
