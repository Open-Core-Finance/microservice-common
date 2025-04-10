package tech.corefinance.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.annotation.PermissionResource;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.dto.UserRoleDto;
import tech.corefinance.common.entity_author.Permission;
import tech.corefinance.common.enums.AccessControl;
import tech.corefinance.common.repository.PermissionRepository;
import tech.corefinance.common.service.InternalApiVerify;
import tech.corefinance.common.service.ResourceOwnerVerifier;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Framework to verify API permission.
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
@Slf4j
public class ApiAuthorizationCheck {

    private static final String EXECUTION_EXCLUDED =
            "!@annotation(tech.corefinance.common.annotation.ManualPermissionCheck)";

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private List<RequestMappingHandlerMapping> handlerMappings;
    @Autowired
    private CoreFinanceUtil coreFinanceUtil;
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired(required = false)
    private List<ResourceOwnerVerifier> resourceOwnerVerifiers;
    @Autowired(required = false)
    private List<InternalApiVerify> internalApiVerifiers;

    @Value("${tech.corefinance.security.permission.default-control}")
    private AccessControl permissionDefaultControl;
    @Value("${tech.corefinance.security.exclude-classes-authorize-check:}")
    private List<String> excludeClasses;

    /**
     * Verify for GET HTTP Methods.
     *
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) && " + EXECUTION_EXCLUDED)
    public Object verifyGetRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return verifyRequest(joinPoint, RequestMethod.GET);
    }

    /**
     * Verify for POST HTTP Methods.
     *
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping) && " + EXECUTION_EXCLUDED)
    public Object verifyPostRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return verifyRequest(joinPoint, RequestMethod.POST);
    }

    /**
     * Verify for PUT HTTP Methods.
     *
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PutMapping) && " + EXECUTION_EXCLUDED)
    public Object verifyPutRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return verifyRequest(joinPoint, RequestMethod.PUT);
    }

    /**
     * Verify for PATCH HTTP Methods.
     *
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PatchMapping) && " + EXECUTION_EXCLUDED)
    public Object verifyPatchRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return verifyRequest(joinPoint, RequestMethod.PATCH);
    }

    /**
     * Verify for DELETE HTTP Methods.
     *
     * @param joinPoint Method call
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.DeleteMapping) && " + EXECUTION_EXCLUDED)
    public Object verifyDeleteRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return verifyRequest(joinPoint, RequestMethod.DELETE);
    }

    /**
     * Verify request authentication.
     *
     * @param joinPoint This is param of AOP. Just ignore it
     * @return result of the wrapped services method
     * @throws Throwable when target method have exception
     */
    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) && " + EXECUTION_EXCLUDED)
    public Object verifyGenericRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return verifyRequest(joinPoint, RequestMethod.valueOf(request.getMethod()));
    }

    /**
     * Verify for HTTP Methods.
     *
     * @param joinPoint     Method call
     * @param requestMethod Runtime HTTP method.
     * @return Method response
     * @throws Throwable Method exception or error.
     */
    public Object verifyRequest(ProceedingJoinPoint joinPoint, RequestMethod requestMethod) throws Throwable {
        JwtTokenDto jwtTokenDto = JwtContext.getInstance().getJwt();
        if (jwtTokenDto != null) {
            var target = joinPoint.getTarget();
            var controllerClass = target.getClass();
            String controllerClassName = controllerClass.getName();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            log.debug("Controller class [{}]", controllerClass);
            if (Proxy.isProxyClass(controllerClass)) {
                var unProxyObj = coreFinanceUtil.unProxy(target);
                if (unProxyObj != null) {
                    controllerClass = unProxyObj.getClass();
                    log.debug("UnProxy target class [{}]", controllerClass);
                    log.debug("Checking with ignored list {}", excludeClasses);
                    if (coreFinanceUtil.isMatchedInstanceType(unProxyObj, excludeClasses)) {
                        log.debug("Excluded for [{}]", unProxyObj);
                        return joinPoint.proceed();
                    }
                }
            }

            log.debug("Verifying method [{}#{}]", controllerClassName, signature.getName());
            Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethodEntry = resolveHandlerInfo(method);
            if (handlerMethodEntry == null) {
                log.error("This error should not happen. If it happens, please check AOP condition.");
                throw new IllegalStateException("unknown_method_handler");
            }
            String url = resolveUrl(handlerMethodEntry, request);
            log.debug("Resolved URL [{}] for request URI [{}]", url, request.getRequestURI());
            var perActAnn = method.getAnnotation(PermissionAction.class);
            var action = coreFinanceUtil.resolveResourceAction(perActAnn, handlerMethodEntry.getKey());
            var controllerManagedResource = controllerClass.getAnnotation(ControllerManagedResource.class);
            var resourceType = coreFinanceUtil.resolveResourceType(perActAnn, controllerManagedResource);
            Collection<UserRoleDto> userRoles = jwtTokenDto.getUserRoles();
            var foundMatched = false;
            var isAdmin = false;
            for (var userRole : userRoles) {
                if ("SystemAdmin".equalsIgnoreCase(userRole.getRoleId())) {
                    isAdmin = true;
                    break;
                }
            }
            if (!isAdmin) {
                var internalCheckPass = true;
                if (internalApiVerifiers != null) {
                    for (var verifier : internalApiVerifiers) {
                        if (!verifier.internalPermissionCheck(controllerClass, method, request)) {
                            internalCheckPass = false;
                            break;
                        }
                    }
                }
                if (internalCheckPass) {
                    for (var userRole : userRoles) {
                        if (verifyRole(jwtTokenDto, userRole, action, url, requestMethod, resourceType, joinPoint)) {
                            foundMatched = true;
                        }
                    }
                }
                if (!foundMatched && AccessControl.ALLOWED != permissionDefaultControl) {
                    throw new AccessDeniedException("no_permission_config");
                }
            }
        }
        return joinPoint.proceed();
    }

    private Map.Entry<RequestMappingInfo, HandlerMethod> resolveHandlerInfo(Method method) {
        for (var mapping : handlerMappings) {
            var handlerMethods = mapping.getHandlerMethods();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                var value = entry.getValue();
                if (method.equals(value.getMethod())) {
                    return entry;
                }
            }
        }
        return null;
    }

    private String resolveUrl(Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethodEntry, HttpServletRequest request) {
        Set<Pair<String, String>> urls = coreFinanceUtil.buildUrlPair(handlerMethodEntry.getKey().getPatternValues());
        String result = null;
        for (var url : urls) {
            if (RegexRequestMatcher.regexMatcher(url.getSecond()).matches(request)) {
                result = url.getSecond();
                break;
            } else if (result == null) {
                result = url.getSecond();
            }
        }
        return result;
    }

    private boolean verifyRole(JwtTokenDto jwtTokenDto, UserRoleDto userRole, String action, String url,
                               RequestMethod requestMethod, String resourceType, ProceedingJoinPoint joinPoint) {
        var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
        var permissions = permissionRepository.findAllByRoleIdAndResourceType(userRole.getRoleId(), resourceType, sort);
        var foundMatched = false;
        for (var permission : permissions) {
            var matchedAction = Permission.ANY_ROLE_APPLIED_VALUE.equalsIgnoreCase(permission.getAction()) ||
                    action.equalsIgnoreCase(permission.getAction());
            var matchUrl = Permission.ANY_ROLE_APPLIED_VALUE.equalsIgnoreCase(permission.getUrl()) ||
                    url.equalsIgnoreCase(permission.getUrl());
            var matchedRequestMethod =
                    permission.getRequestMethod() == null || permission.getRequestMethod() == requestMethod;
            if (matchedAction && matchUrl && matchedRequestMethod) {
                foundMatched = true;
                checkPermissionControl(jwtTokenDto, permission, joinPoint, userRole);
            }
        }
        return foundMatched;
    }

    private ResourceInfoPair resolveResourceId(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        var annotations = method.getParameterAnnotations();
        log.debug("ANN [{}]", new Object[]{annotations});
        PermissionResource permissionResource = null;
        Object parameterValue = null;
        main_loop:
        for (int i = 0; i < annotations.length; i++) {
            for (var annotation : annotations[i]) {
                if (PermissionResource.class.isAssignableFrom(annotation.annotationType())) {
                    permissionResource = (PermissionResource) annotation;
                    parameterValue = joinPoint.getArgs()[i];
                    break main_loop;
                }
            }
        }
        if (permissionResource == null) {
            throw new AccessDeniedException("no_resource_id_to_verify_permission");
        }
        if (!StringUtils.hasText(permissionResource.idPath())) {
            return new ResourceInfoPair(permissionResource.resourceType(), parameterValue);
        } else {
            if (parameterValue == null) {
                return new ResourceInfoPair(permissionResource.resourceType(), null);
            }
            var fieldPath = permissionResource.idPath();
            var extractedValue = coreFinanceUtil.getDeepAttributeValue(parameterValue, fieldPath);
            return new ResourceInfoPair(permissionResource.resourceType(), extractedValue);
        }
    }

    private boolean checkResourceOwnership(JwtTokenDto jwtTokenDto, String resourceType, Object resourceId,
                                           Permission permission, UserRoleDto userRole) {
        var result = false;
        for (var verifier : resourceOwnerVerifiers) {
            if (verifier.isSupportedResource(resourceType)) {
                result = verifier.verifyOwner(jwtTokenDto, resourceType, resourceId, permission, userRole);
                break;
            }
        }
        return result;
    }

    private void checkPermissionControl(JwtTokenDto jwtTokenDto, Permission permission, ProceedingJoinPoint joinPoint,
                                        UserRoleDto userRole) {
        var control = permission.getControl();
        switch (control) {
            case DENIED -> throw new AccessDeniedException("access_denied_" + permission.getId());
            case ALLOWED_SPECIFIC_RESOURCES -> {
                var resourceInfo = resolveResourceId(joinPoint);
                var matched = checkResourceOwnership(jwtTokenDto, resourceInfo.resourceType, resourceInfo.resourceId,
                        permission, userRole);
                if (!matched) {
                    throw new AccessDeniedException("access_denied_" + permission.getId());
                }
            }
            case DENIED_SPECIFIC_RESOURCES -> {
                var resourceInfo = resolveResourceId(joinPoint);
                var matched = checkResourceOwnership(jwtTokenDto, resourceInfo.resourceType, resourceInfo.resourceId,
                        permission, userRole);
                if (matched) {
                    throw new AccessDeniedException("access_denied_" + permission.getId());
                }
            }
        }
    }

    record ResourceInfoPair(String resourceType, Object resourceId) {
    }
}
