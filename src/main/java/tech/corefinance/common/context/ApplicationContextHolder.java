package tech.corefinance.common.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHolder implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static ApplicationContextHolder INSTANCE = new ApplicationContextHolder();

    private ApplicationContext applicationContext;

    private ApplicationContextHolder() {
        // Empty private constructor for singleton design.
        logger.debug("Created ApplicationContextHolder [{}]", this);
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
