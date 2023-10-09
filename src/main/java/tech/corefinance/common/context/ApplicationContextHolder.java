package tech.corefinance.common.context;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * To get Spring ApplicationContext at any place within system.
 */
@Slf4j
@Getter
@Setter
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContextHolder INSTANCE = new ApplicationContextHolder();

    private ApplicationContext applicationContext;

    /**
     * Private constructor for singleton implementation.
     */
    private ApplicationContextHolder() {
        // Empty private constructor for singleton design.
        log.debug("Created ApplicationContextHolder [{}]", this);
    }

    /**
     * Singleton implementation.
     * @return Single instance within application.
     */
    public static ApplicationContextHolder getInstance() {
        return INSTANCE;
    }
}
