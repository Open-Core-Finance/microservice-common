package tech.corefinance.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collections;
import java.util.List;

/**
 * Logging for APIs (Controllers).
 */
@Aspect
@Component
@ConditionalOnProperty(name = "tech.corefinance.log.enabled.api", havingValue = "true", matchIfMissing = true)
@Slf4j
public class ApiLogging extends MethodDataLoging {

    @Autowired
    private HttpServletRequest request;

    public ApiLogging(@Value("${tech.corefinance.log.exclude-classes:feign.Target}") List<String> excludeClasses) {
        super(excludeClasses);
    }

    /**
     * Generate logs for GET HTTP Methods.
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object logGetRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    /**
     * Generate logs for POST HTTP Methods.
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object logPostRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    /**
     * Generate logs for PUT HTTP Methods.
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public Object logPutRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    /**
     * Generate logs for PATCH HTTP Methods.
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public Object verifyPatchRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    /**
     * Generate logs for DELETE HTTP Methods.
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
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
    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return doLogging(joinPoint);
    }

    /**
     * Additional log for input if needed.
     * @param joinPoint Calling method.
     * @param jsonMapper System object mapper.
     */
    @Override
    protected void doAdditionalInputLog(ProceedingJoinPoint joinPoint, JsonMapper jsonMapper) {
        log.debug("== Request header <= START");
        log.debug("API: [{}] - [{}]", request.getMethod(), request.getServletPath());
        Collections.list(request.getHeaderNames()).forEach(h -> log.debug("Name: [{}] - Value: [{}]", h, request.getHeader(h)));
        log.debug("== Request header <=   END");
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
