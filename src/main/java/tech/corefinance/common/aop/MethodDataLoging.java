package tech.corefinance.common.aop;

import tech.corefinance.common.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class MethodDataLoging {

    @Autowired
    protected Util util;
    @Autowired
    protected ObjectMapper objectMapper;

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodDataLoging.class);

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
            LOGGER.info(msg.toString());

            // Input
            String[] parametersNames = signature.getParameterNames();
            String input = util.buildMethodInputJsonLog(joinPoint, parametersNames, objectMapper);
            LOGGER.info("Input [{}]", input);
            doAdditionalInputLog(joinPoint, objectMapper);

            // Process the method.
            start = System.currentTimeMillis();
            Class<?> returnType = signature.getReturnType();
            LOGGER.info("Return type {}", returnType);
            Object result = joinPoint.proceed();
            end = System.currentTimeMillis();
            if (!void.class.isAssignableFrom(returnType)) {
                LOGGER.debug("Result: {}", util.writeValueToJson(objectMapper, result));
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
            LOGGER.info(msg.toString());
        }
    }

    protected void doAdditionalInputLog(ProceedingJoinPoint joinPoint, ObjectMapper objectMapper) {
    }

}
