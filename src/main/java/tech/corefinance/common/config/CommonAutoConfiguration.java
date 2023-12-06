package tech.corefinance.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import tech.corefinance.common.context.*;
import tech.corefinance.common.dto.SimpleVersionComparator;

@Configuration
@ConditionalOnProperty(prefix = "tech.corefinance.common.enabled", name = "context", havingValue = "true", matchIfMissing = true)
@Slf4j
public class CommonAutoConfiguration {

    @Bean(name = "localeResolver")
    @ConditionalOnMissingBean(LocaleResolver.class)
    public LocaleResolver getLocaleResolver() {
        log.info("Creating StatelessLocaleResolver...");
        return new StatelessLocaleResolver();
    }

    @Bean
    @ConditionalOnMissingBean(ApplicationContextHolder.class)
    public ApplicationContextHolder applicationContextHolder() {
        log.info("Creating ApplicationContextHolder...");
        return ApplicationContextHolder.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean(JwtContext.class)
    public JwtContext jwtContext() {
        log.info("Creating JwtContext...");
        return JwtContext.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean(SimpleVersionComparator.class)
    @ConditionalOnProperty(prefix = "tech.corefinance.common.enabled", name = "simple-version", havingValue = "true")
    public SimpleVersionComparator simpleVersionComparator() {
        log.info("Creating SimpleVersionComparator...");
        return new SimpleVersionComparator();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        log.info("Creating BCryptPasswordEncoder...");
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(prefix = "tech.corefinance.common.enabled", name = "tenant", havingValue = "true")
    public TenantContext tenantContext() {
        log.info("Creating TenantContext...");
        return TenantContext.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean(TraceIdContext.class)
    public TraceIdContext traceIdContext() {
        log.info("Creating TraceIdContext...");
        return TraceIdContext.getInstance();
    }
}
