package tech.corefinance.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tech.corefinance.common.entity_author.AnonymousUrlAccess;
import tech.corefinance.common.filter.SessionAuthenticationFilter;
import tech.corefinance.common.repository.AnonymousUrlAccessRepository;

import java.util.List;

@Configuration
@Slf4j
public class WebSecurityConfig {

    @Autowired
    private ServiceSecurityConfig serviceSecurityConfig;
    @Autowired(required = false)
    private AnonymousUrlAccessRepository anonymousUrlAccessRepository;
    @Autowired(required = false)
    private UrlBasedCorsConfiguration urlBasedCorsConfiguration;

    @Bean
    @ConditionalOnProperty(prefix = "tech.corefinance.security", name = "public-key")
    public SecurityFilterChain filterChain(HttpSecurity http, SessionAuthenticationFilter sessionAuthenticationFilter)
            throws Exception {

        http.csrf(AbstractHttpConfigurer::disable).cors(c -> c.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> serviceSecurityConfig.getNoAuthenUrls().forEach(url -> auth
                        .requestMatchers(url).permitAll())
                );
        if (anonymousUrlAccessRepository != null) {
            List<AnonymousUrlAccess> anonymousList = anonymousUrlAccessRepository.findAll();
            http.authorizeHttpRequests(auth -> anonymousList.forEach(p -> registerPermitAllAccess(auth, p)))
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .addFilterBefore(sessionAuthenticationFilter, AnonymousAuthenticationFilter.class);
        } else {
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        }
        return http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
    }

    private void registerPermitAllAccess(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth,
            AnonymousUrlAccess anonymousUrlAccess) {
        HttpMethod httpMethod = null;
        var requestMethod = anonymousUrlAccess.getRequestMethod();
        if (requestMethod != null) {
            httpMethod = requestMethod.asHttpMethod();
        }
        auth.requestMatchers(httpMethod, anonymousUrlAccess.getUrl()).permitAll();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if (urlBasedCorsConfiguration != null) {
            urlBasedCorsConfiguration.getCorsConfigurations().forEach(t -> {
                log.debug("CORS Configuration for [{}] is [AllowedOrigins={},AllowedHeaders={}, AllowedMethods={}]",
                        t.getUrlPattern(), t.getAllowedOrigins(), t.getAllowedHeaders(), t.getAllowedMethods());
                source.registerCorsConfiguration(t.getUrlPattern(), t);
            });
        }
        return source;
    }
}
