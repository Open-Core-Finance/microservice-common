package tech.corefinance.common.jpa.tenancy;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "tech.corefinance.schema-tenancy")
@Data
public class TenancyProperties {
    private boolean enabled;
    private String mode;
    private String schemaPrefix;
    private String defaultSchema = "public";
    private boolean validateExistingCurrentSessions = true;
    private List<String> ignoreSchemas = new LinkedList<>();
}
