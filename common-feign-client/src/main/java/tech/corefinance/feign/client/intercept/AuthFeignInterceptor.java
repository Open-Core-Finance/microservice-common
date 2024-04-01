package tech.corefinance.feign.client.intercept;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.dto.JwtTokenDto;

import static tech.corefinance.common.enums.CommonConstants.*;

@Slf4j
public class AuthFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        JwtTokenDto jwtTokenDto = JwtContext.getInstance().getJwt();
        if (jwtTokenDto != null) {
            log.debug("Found JWT token in context!");
            template.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTokenDto.getOriginalToken());
            log.debug("Forwarded device ID [{}]", jwtTokenDto.getDeviceId());
            template.header(DEVICE_ID, jwtTokenDto.getDeviceId());
            log.debug("Forwarded ip address [{}]", jwtTokenDto.getLoginIpAddr());
            template.header(HEADER_KEY_EXTERNAL_IP_ADDRESS, jwtTokenDto.getLoginIpAddr());
            log.debug("Forwarded client app ID [{}]", jwtTokenDto.getClientAppId());
            template.header(HEADER_KEY_CLIENT_APP_ID, jwtTokenDto.getClientAppId());
            var appPlatform = jwtTokenDto.getAppPlatform();
            if (appPlatform != null) {
                log.debug("Forwarded client app platform [{}]", appPlatform.name());
                template.header(HEADER_KEY_APP_PLATFORM, appPlatform.name());
            }
            var appVersion = jwtTokenDto.getAppVersion();
            if (appVersion != null) {
                log.debug("Forwarded client app version [{}]", appVersion);
                template.header(HEADER_KEY_APP_VERSION, appVersion.toString());
            }
        } else {
            log.debug("Didn't found JWT token in context!");
        }
    }
}
