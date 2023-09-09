package com.finance.core.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@ConditionalOnProperty(prefix = "com.finance.core.common.enabled", name = "async", havingValue = "true", matchIfMissing = true)
public class AsyncConfig {
}
