package tech.corefinance.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * Logging service calls.
 */
@Aspect
@Component
@ConditionalOnProperty(name = "tech.corefinance.log.enabled.services", havingValue = "true", matchIfMissing = true)
public class ServiceLogging extends MethodDataLoging {

    public ServiceLogging() {
        super(new LinkedList<>());
    }

    /**
     * Mark in the log for reading purpose.
     * @return Mark in the log for reading purpose.
     */
    @Override
    protected String getLogingStartMark() {
        return "***********";
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice.
     *
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around("within(@org.springframework.stereotype.Service *)")
    private Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

}
