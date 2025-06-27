package tech.corefinance.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "export")
@Data
public class ExportingGlobalConfig {
    private ExportingDateFormatConfig format;
}
