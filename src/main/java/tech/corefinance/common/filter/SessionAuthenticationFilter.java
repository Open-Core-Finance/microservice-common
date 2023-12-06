package tech.corefinance.common.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.service.JwtService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "public-key")
@Component
@Slf4j
public class SessionAuthenticationFilter extends OncePerRequestFilter implements Ordered {

    @Autowired
    private JwtService jwtService;
    @Value("${tech.corefinance.common.filter-ordered.authen-filter:2}")
    private int ordered;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Map<String, JwtTokenDto> map = jwtService.retreiveTokenFromRequest(request, response);
            String tokenString = "";
            JwtTokenDto jwtTokenDto = null;
            Set<String> set = map.keySet();
            for (String key : set){
                tokenString = key;
                jwtTokenDto = map.get(key);
            }
            if (jwtTokenDto != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtTokenDto.getUsername(), null,
                        getAuthorities(jwtTokenDto));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                JwtContext.getInstance().setJwt(jwtTokenDto);
                JwtContext.getInstance().setTokenString(tokenString);
            }
            filterChain.doFilter(request, response);
        } catch (BadCredentialsException | JWTVerificationException e) {
            log.debug("Error", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            String message = e.getMessage();
            response.setContentLength(message.length());
            try (OutputStream ops = response.getOutputStream(); PrintWriter pw = new PrintWriter(
                    new OutputStreamWriter(ops, StandardCharsets.UTF_8))) {
                pw.print(message);
            }
        } finally {
            JwtContext.getInstance().removeJwt();
            JwtContext.getInstance().removeTokenString();
        }
    }

    private Set<GrantedAuthority> getAuthorities(JwtTokenDto jwtTokenDto) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(jwtTokenDto.getUserRoles());
        return authorities;
    }

    @Override
    public int getOrder() {
        return ordered;
    }
}
