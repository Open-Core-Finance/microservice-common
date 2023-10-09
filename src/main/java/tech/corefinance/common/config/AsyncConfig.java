package tech.corefinance.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * To enable/disable async just set tech.corefinance.common.enabled.async=true/false.
 */
@Configuration
@EnableAsync
@ConditionalOnProperty(prefix = "tech.corefinance.common.enabled", name = "async", havingValue = "true", matchIfMissing = true)
public class AsyncConfig {
}
