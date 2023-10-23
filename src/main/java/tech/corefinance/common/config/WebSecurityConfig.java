package tech.corefinance.common.config;

import tech.corefinance.common.filter.SessionAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tech.corefinance.common.model.AnonymousUrlAccess;
import tech.corefinance.common.model.Permission;
import tech.corefinance.common.repository.AnonymousUrlAccessRepository;
import tech.corefinance.common.repository.PermissionRepository;

import java.util.List;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private ServiceSecurityConfig serviceSecurityConfig;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private AnonymousUrlAccessRepository anonymousUrlAccessRepository;

    @Bean
    @ConditionalOnProperty(prefix = "tech.corefinance.security", name = "public-key")
    public SecurityFilterChain filterChain(HttpSecurity http, SessionAuthenticationFilter sessionAuthenticationFilter) throws Exception {
        List<AnonymousUrlAccess> anonymousList = anonymousUrlAccessRepository.findAll();
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> serviceSecurityConfig.getNoAuthenUrls().stream().forEach(url -> auth
                        .requestMatchers(url).permitAll())
                ).authorizeHttpRequests(auth -> anonymousList.stream().forEach(p ->
                        auth.requestMatchers(p.getUrl()).anonymous()))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(sessionAuthenticationFilter, AnonymousAuthenticationFilter.class)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
