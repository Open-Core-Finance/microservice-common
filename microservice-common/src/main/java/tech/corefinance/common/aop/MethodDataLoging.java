package tech.corefinance.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Abstract method call logging.
 */
public abstract class MethodDataLoging {

    @Autowired
    protected CoreFinanceUtil coreFinanceUtil;
    @Autowired
    protected ObjectMapper objectMapper;
    private List<String> excludeClasses;

    protected abstract Logger getLog();

    public MethodDataLoging(List<String> excludeClasses) {
        this.excludeClasses = excludeClasses;
    }

    /**
     * Mark in the log for reading purpose.
     * @return Mark in the log for reading purpose.
     */
    protected String getLogingStartMark() {
        return "========================";
    }

    /**
     * Main logging method.
     * @param joinPoint Method call.
     * @return Method response.
     * @throws Throwable Method execption or error.
     */
    public Object doLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        var log = getLog();
        // Advice
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // Start mark
        String startMark = getLogingStartMark();
        var target = joinPoint.getTarget();
        var targetClass = target.getClass();
        log.debug("Target class [{}]", targetClass);
        if (Proxy.isProxyClass(targetClass)) {
            var unProxyObj = coreFinanceUtil.unProxy(target);
            if (unProxyObj != null) {
                targetClass = unProxyObj.getClass();
                log.debug("UnProxy target class [{}]", targetClass);
                if (coreFinanceUtil.isMatchedInstanceType(unProxyObj, excludeClasses)) {
                    log.debug("Excluded for [{}]", unProxyObj);
                    return joinPoint.proceed();
                }
            }
        }
        StringBuilder msg = new StringBuilder(startMark).append(targetClass.getSimpleName()).append("#").append(
                signature.getName()).append(startMark + " <= ");
        int length = msg.length();
        long start = 0, end = 0;

        try {
            msg.append("START");
            log.info(msg.toString());

            // Input
            String[] parametersNames = signature.getParameterNames();
            String input = coreFinanceUtil.buildMethodInputJsonLog(joinPoint, parametersNames, objectMapper);
            log.info("Input [{}]", input);
            doAdditionalInputLog(joinPoint, objectMapper);

            // Process the method.
            start = System.currentTimeMillis();
            Class<?> returnType = signature.getReturnType();
            log.info("Return type {}", returnType);
            Object result = joinPoint.proceed();
            end = System.currentTimeMillis();
            if (!void.class.isAssignableFrom(returnType)) {
                log.debug("Result: {}", coreFinanceUtil.writeValueToJson(objectMapper, result));
            }
            // Return
            return result;
        } finally {
            if (end == 0) {
                end = System.currentTimeMillis();
            }
            // End mark
            msg.setLength(length);
            msg.append("  END in [").append(end - start).append("] ms.");
            log.info(msg.toString());
        }
    }

    /**
     * Additional log for input if needed.
     * @param joinPoint Calling method.
     * @param objectMapper System object mapper.
     */
    protected void doAdditionalInputLog(ProceedingJoinPoint joinPoint, ObjectMapper objectMapper) {
    }

}
