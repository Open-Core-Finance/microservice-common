package tech.corefinance.common.context;

import tech.corefinance.common.dto.JwtTokenDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtContext {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ThreadLocal<JwtTokenDto> tokenThreadLocal = new ThreadLocal<>();
    private ThreadLocal<String> tokenStringThreadLocal = new ThreadLocal<>();
    private static final JwtContext INSTANCE = new JwtContext();

    private JwtContext() {
        // Singleton implement
        logger.debug("Created PermissionContext [{}]", this);
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
