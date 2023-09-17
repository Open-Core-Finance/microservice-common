package tech.corefinance.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Logging for APIs (Controllers).
 */
@Aspect
@Component
@ConditionalOnProperty(name = "tech.corefinance.log.enabled.api", havingValue = "true", matchIfMissing = true)
@Slf4j
public class ApiLogging extends MethodDataLoging {

    private static final String EXECUTION_FEIGN_CLIENT = "execution(* *Client(..))";
    private static final String EXECUTION_FEIGN_CLIENT_EXCLUDED = "!" + EXECUTION_FEIGN_CLIENT;

    @Autowired
    private HttpServletRequest request;

    /**
     * Generate logs for GET HTTP Methods.
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) && " + EXECUTION_FEIGN_CLIENT_EXCLUDED)
    public Object logGetRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    /**
     * Generate logs for POST HTTP Methods.
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping) && " + EXECUTION_FEIGN_CLIENT_EXCLUDED)
    public Object logPostRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    /**
     * Generate logs for PUT HTTP Methods.
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PutMapping) && " + EXECUTION_FEIGN_CLIENT_EXCLUDED)
    public Object logPutRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    /**
     * Generate logs for PATCH HTTP Methods.
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PatchMapping) && " + EXECUTION_FEIGN_CLIENT_EXCLUDED)
    public Object verifyPatchRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    /**
     * Generate logs for DELETE HTTP Methods.
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
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

    /**
     * Additional log for input if needed.
     * @param joinPoint Calling method.
     * @param objectMapper System object mapper.
     */
    @Override
    protected void doAdditionalInputLog(ProceedingJoinPoint joinPoint, ObjectMapper objectMapper) {
        log.debug("== Request header <= START");
        log.debug("API: [{}] - [{}]", request.getMethod(), request.getServletPath());
        Collections.list(request.getHeaderNames()).stream().forEach(h -> log.debug("Name: [{}] - Value: [{}]", h, request.getHeader(h)));
        log.debug("== Request header <=   END");
    }

}
