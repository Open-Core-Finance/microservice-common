package tech.corefinance.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tech.corefinance.initial")
@Data
public class InitDataConfiguration {
    private String nameSeparator;
    private String versionSeparator;
}
