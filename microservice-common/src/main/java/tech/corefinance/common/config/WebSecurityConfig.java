package tech.corefinance.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tech.corefinance.common.filter.SessionAuthenticationFilter;
import tech.corefinance.common.model.AnonymousUrlAccess;
import tech.corefinance.common.repository.AnonymousUrlAccessRepository;

import java.util.List;

@Configuration
@Slf4j
public class WebSecurityConfig {

    @Autowired
    private ServiceSecurityConfig serviceSecurityConfig;
    @Autowired
    private AnonymousUrlAccessRepository anonymousUrlAccessRepository;
    @Autowired(required = false)
    private UrlBasedCorsConfiguration urlBasedCorsConfiguration;

    @Bean
    @ConditionalOnProperty(prefix = "tech.corefinance.security", name = "public-key")
    public SecurityFilterChain filterChain(HttpSecurity http, SessionAuthenticationFilter sessionAuthenticationFilter)
            throws Exception {
        List<AnonymousUrlAccess> anonymousList = anonymousUrlAccessRepository.findAll();
        return http.csrf(csrf -> csrf.disable()).cors(c -> c.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> serviceSecurityConfig.getNoAuthenUrls().stream().forEach(url -> auth
                        .requestMatchers(url).permitAll())
                ).authorizeHttpRequests(auth -> anonymousList.stream().forEach(p -> registerPermitAllAccess(auth, p)))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(sessionAuthenticationFilter, AnonymousAuthenticationFilter.class)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
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
