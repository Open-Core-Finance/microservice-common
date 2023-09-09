package com.finance.core.common.config;

import com.finance.core.common.dto.BasicUserDto;
import com.finance.core.common.dto.SimpleVersionComparator;
import com.finance.core.common.context.ApplicationContextHolder;
import com.finance.core.common.context.JwtContext;
import com.finance.core.common.context.StatelessLocaleResolver;
import com.finance.core.common.services.JwtService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Optional;

@Configuration
@ConditionalOnProperty(prefix = "kbtg.common.enabled", name = "context", havingValue = "true", matchIfMissing = true)
public class CommonAutoConfiguration {

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private List<Converter<?, ?>> listConverter;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean(name = "localeResolver")
    @ConditionalOnMissingBean(LocaleResolver.class)
    public LocaleResolver getLocaleResolver() {
        return new StatelessLocaleResolver();
    }

    @Bean
    @ConditionalOnMissingBean(ApplicationContextHolder.class)
    public ApplicationContextHolder applicationContextHolder() {
        return ApplicationContextHolder.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean(JwtContext.class)
    public JwtContext jwtContext() {
        return JwtContext.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean(SimpleVersionComparator.class)
    @ConditionalOnProperty(prefix = "kbtg.common.enabled", name = "simple-version", havingValue = "true")
    public SimpleVersionComparator simpleVersionComparator() {
        return new SimpleVersionComparator();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuditorAware<BasicUserDto> userAuditorAware(JwtService jwtService) {
        var user = jwtService.retrieveUserAsAttribute(JwtContext.getInstance().getJwt());
        if (user == null) {
            return () -> Optional.empty();
        }
        return () -> Optional.of(user);
    }

    @PostConstruct
    public void postConstruct() {
        // Setting up web converters
        listConverter.forEach(c -> {
            ((GenericConversionService) conversionService).addConverter(c);
            logger.debug("Added converter {}", c);
        });
    }
}
