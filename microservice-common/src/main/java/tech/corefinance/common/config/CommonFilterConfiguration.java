package tech.corefinance.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "tech.corefinance.filter")
@AllArgsConstructor
@Data
@Configuration
public class CommonFilterConfiguration {
    private final List<String> ignoreTenantControllers;
}
