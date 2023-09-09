package com.finance.core.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "com.finance.core.security")
@Data
public class ServiceSecurityConfig {

    private List<String> noAuthenUrls = new LinkedList<>();
    private boolean authorizeCheck;
    private boolean scanControllersActions;
    private List<String> ignoreControllerScan;
}
