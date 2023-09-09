package com.finance.core.common.test.support.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.finance.core.encryption")
@Data
public class EncryptedConfig {
    private String plain;
    private String encoded;
}
