package tech.corefinance.common.context;

import lombok.extern.slf4j.Slf4j;
import tech.corefinance.common.dto.JwtTokenDto;

@Slf4j
public class JwtContext {

    private ThreadLocal<JwtTokenDto> tokenThreadLocal = new ThreadLocal<>();
    private ThreadLocal<String> tokenStringThreadLocal = new ThreadLocal<>();
    private static final JwtContext INSTANCE = new JwtContext();

    private JwtContext() {
        // Singleton implement
        log.debug("Created PermissionContext [{}]", this);
    }

    public static JwtContext getInstance() {
        return INSTANCE;
    }

    public void setJwt(JwtTokenDto jwt) {
        tokenThreadLocal.set(jwt);
    }

    public void removeJwt() {
        tokenThreadLocal.remove();
    }

    public JwtTokenDto getJwt() {
        return tokenThreadLocal.get();
    }

    public void setTokenString(String tokenString) {
        tokenStringThreadLocal.set(tokenString);
    }

    public void removeTokenString() {
        tokenStringThreadLocal.remove();
    }

    public String getTokenString() {
        return tokenStringThreadLocal.get();
    }
}
