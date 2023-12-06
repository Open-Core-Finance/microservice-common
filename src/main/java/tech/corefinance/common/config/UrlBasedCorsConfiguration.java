package tech.corefinance.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.LinkedList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "tech.corefinance.security.url-cors")
@Data
public class UrlBasedCorsConfiguration {
    private List<UrlPatternCorsConfiguration> corsConfigurations = new LinkedList<>();

    @Data
    public static class UrlPatternCorsConfiguration extends CorsConfiguration {
        private String urlPattern;
    }
}
