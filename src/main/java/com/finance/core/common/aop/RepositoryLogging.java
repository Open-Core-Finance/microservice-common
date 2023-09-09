package com.finance.core.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(name = "com.finance.core.log.enabled.repositories", havingValue = "true", matchIfMissing = true)
public class RepositoryLogging extends MethodDataLoging {

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

}
