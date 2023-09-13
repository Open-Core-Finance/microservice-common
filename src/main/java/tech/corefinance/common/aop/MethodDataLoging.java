package tech.corefinance.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import tech.corefinance.common.util.CoreFinanceUtil;

@Slf4j
public abstract class MethodDataLoging {

    @Autowired
    protected CoreFinanceUtil coreFinanceUtil;
    @Autowired
    protected ObjectMapper objectMapper;

    protected String getLogingStartMark() {
        return "========================";
    }

    public Object doLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        // Advice
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // Start mark
        String startMark = getLogingStartMark();
        StringBuilder msg = new StringBuilder(startMark).append(joinPoint.getTarget().getClass().getSimpleName()).append("#").append(
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

    protected void doAdditionalInputLog(ProceedingJoinPoint joinPoint, ObjectMapper objectMapper) {
    }

}
