package tech.corefinance.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "tech.corefinance.security")
@Data
public class ServiceSecurityConfig {

    private List<String> noAuthenUrls = new LinkedList<>();
    private boolean authorizeCheck;
    private boolean scanControllersActions;
    private List<String> ignoreControllerScan;
    private boolean requireControllerWithAnnotation = true;
}
