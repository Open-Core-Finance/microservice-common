package tech.corefinance.common.test.support.aop;

import tech.corefinance.common.aop.ApiLogging;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class AspectConfig {

    @Autowired
    private ApiLogging apiLogging;

    @Pointcut("execution(* tech.corefinance.common.test.support.aop.AopTestController.*(..))")
    public void testControllerCut() {
    }

    @Around("testControllerCut()")
    public Object testControllerInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        return apiLogging.logAround(joinPoint);
    }

}
