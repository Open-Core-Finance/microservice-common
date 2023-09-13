package tech.corefinance.common.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Slf4j
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContextHolder INSTANCE = new ApplicationContextHolder();

    private ApplicationContext applicationContext;

    private ApplicationContextHolder() {
        // Empty private constructor for singleton design.
        log.debug("Created ApplicationContextHolder [{}]", this);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public static ApplicationContextHolder getInstance() {
        return INSTANCE;
    }
}
