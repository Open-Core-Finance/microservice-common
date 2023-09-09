package com.finance.core.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;

@Aspect
@Component
@ConditionalOnProperty(name = "kbtg.log.enabled.api", havingValue = "true", matchIfMissing = true)
public class ApiLogging extends MethodDataLoging {

    private static final String EXECUTION_FEIGN_CLIENT = "execution(* *Client(..))";
    private static final String EXECUTION_FEIGN_CLIENT_EXCLUDED = "!" + EXECUTION_FEIGN_CLIENT;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLogging.class);

    @Autowired
    private HttpServletRequest request;

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) && " + EXECUTION_FEIGN_CLIENT_EXCLUDED)
    public Object logGetRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping) && " + EXECUTION_FEIGN_CLIENT_EXCLUDED)
    public Object logPostRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PutMapping) && " + EXECUTION_FEIGN_CLIENT_EXCLUDED)
    public Object logPutRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PatchMapping) && " + EXECUTION_FEIGN_CLIENT_EXCLUDED)
    public Object verifyPatchRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.DeleteMapping) && " + EXECUTION_FEIGN_CLIENT_EXCLUDED)
    public Object verifyDeleteRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    /**
     * Logging.
     *
     * @param joinPoint This is param of AOP. Just ignore it
     *
     * @return result of the wrapped services method
     * @throws Throwable when target method have exception
     */
    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) && " + EXECUTION_FEIGN_CLIENT_EXCLUDED)
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    @Override
    protected void doAdditionalInputLog(ProceedingJoinPoint joinPoint, ObjectMapper objectMapper) {
        LOGGER.debug("== Request header <= START");
        LOGGER.debug("API: [{}] - [{}]", request.getMethod(), request.getServletPath());
        Collections.list(request.getHeaderNames()).stream().forEach(h -> LOGGER.debug("Name: [{}] - Value: [{}]", h, request.getHeader(h)));
        LOGGER.debug("== Request header <=   END");
    }

}
