package tech.corefinance.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Logging repository calls.
 */
@Aspect
@Component
@Slf4j
@ConditionalOnProperty(name = "tech.corefinance.log.enabled.repositories", havingValue = "true", matchIfMissing = true)
public class RepositoryLogging extends MethodDataLoging {

    public RepositoryLogging() {
        super(new LinkedList<>());
    }

    /**
     * Mark in the log for reading purpose.
     * @return Mark in the log for reading purpose.
     */
    @Override
    protected String getLogingStartMark() {
        return "++++++";
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice.
     *
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around("within(@org.springframework.data.repository.* *)")
    private Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
