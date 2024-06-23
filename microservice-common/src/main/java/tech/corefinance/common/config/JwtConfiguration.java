package tech.corefinance.common.config;

import tech.corefinance.common.enums.CommonConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tech.corefinance.security.jwt")
@Data
public class JwtConfiguration {

    private long expiration = 3600;
    private int maxLoginFailAllowed;
    /**
     * See {@link CommonConstants#JWT_VERIFY_MODE_SINGLE_LOGIN}
     * See {@link CommonConstants#JWT_VERIFY_MODE_SINGLE_LOGIN_PER_APP}
     * See {@link CommonConstants#JWT_VERIFY_MODE_SINGLE_LOGIN_PER_DEVICE}
     * See {@link CommonConstants#JWT_VERIFY_MODE_MULTIPLE_LOGIN}
     */
    private String loginMode;
    private boolean skipCompareIpAddress;
}
