package tech.corefinance.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import tech.corefinance.common.context.ApplicationContextHolder;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.context.StatelessLocaleResolver;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.dto.SimpleVersionComparator;
import tech.corefinance.common.service.JwtService;

import java.util.Optional;

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
    public AuditorAware<BasicUserDto> userAuditorAware(JwtService jwtService) {
        log.info("Creating AuditorAware<BasicUserDto>...");
        return () -> {
            var user = jwtService.retrieveUserAsAttribute(JwtContext.getInstance().getJwt());
            if (user == null) {
                log.error("AuditorAware<BasicUserDto> retrieved null data!!");
                return Optional.empty();
            }
            log.error("AuditorAware<BasicUserDto> get user from JwtContext.");
            return Optional.of(user);
        };
    }
}
