package com.finance.core.common.aop;

import com.finance.core.common.annotation.ControllerManagedResource;
import com.finance.core.common.annotation.PermissionAction;
import com.finance.core.common.annotation.PermissionResource;
import com.finance.core.common.context.JwtContext;
import com.finance.core.common.dto.JwtTokenDto;
import com.finance.core.common.dto.UserRoleDto;
import com.finance.core.common.enums.AccessControl;
import com.finance.core.common.model.Permission;
import com.finance.core.common.repository.PermissionRepository;
import com.finance.core.common.services.InternalApiVerify;
import com.finance.core.common.services.ResourceOwnerVerifier;
import com.finance.core.common.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
@ConditionalOnProperty(prefix = "com.finance.core.security", name = "authorize-check", havingValue = "true", matchIfMissing = true)
public class ApiAuthorizationCheck {

    private static final String EXECUTION_FEIGN_CLIENT = "execution(* com.finance.core.rest.client.*Client.*(..))";
    private static final String EXECUTION_FEIGN_CLIENT_EXCLUDED = "!" + EXECUTION_FEIGN_CLIENT;

    private static final String EXECUTION_MANUAL_CHECK_EXCLUDED =
            "!@annotation(com.finance.core.common.annotation.ManualPermissionCheck)";
    private static final String EXECUTION_EXCLUDED =
            EXECUTION_FEIGN_CLIENT_EXCLUDED + " && " + EXECUTION_MANUAL_CHECK_EXCLUDED;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAuthorizationCheck.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RequestMappingHandlerMapping mapping;
    @Autowired
    private Util util;
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired(required = false)
    private List<ResourceOwnerVerifier> resourceOwnerVerifiers;
    @Autowired(required = false)
    private List<InternalApiVerify> internalApiVerifiers;

    @Value("${com.finance.core.security.permission.default-control}")
    private AccessControl permissionDefaultControl;

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) && " + EXECUTION_EXCLUDED)
    public Object verifyGetRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return verifyRequest(joinPoint, RequestMethod.GET);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping) && " + EXECUTION_EXCLUDED)
    public Object verifyPostRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return verifyRequest(joinPoint, RequestMethod.POST);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PutMapping) && " + EXECUTION_EXCLUDED)
    public Object verifyPutRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return verifyRequest(joinPoint, RequestMethod.PUT);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PatchMapping) && " + EXECUTION_EXCLUDED)
    public Object verifyPatchRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return verifyRequest(joinPoint, RequestMethod.PATCH);
    }

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

    public Object verifyRequest(ProceedingJoinPoint joinPoint, RequestMethod requestMethod) throws Throwable {
        JwtTokenDto jwtTokenDto = JwtContext.getInstance().getJwt();
        if (jwtTokenDto != null) {
            var controllerClass = joinPoint.getTarget().getClass();
            String controllerClassName = controllerClass.getName();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String requestUri = request.getRequestURI();
            LOGGER.debug("Verifying method [{}#{}]", controllerClassName, signature.getName());
            Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethodEntry = resolveHandlerInfo(method);
            if (handlerMethodEntry == null) {
                LOGGER.error("This error should not happen. If it happens, please check AOP condition.");
                throw new IllegalStateException("unknown_method_handler");
            }
            String url = resolveUrl(handlerMethodEntry, requestUri);
            LOGGER.debug("Resolved URL [{}] for request URI [{}]", url, requestUri);
            var perActAnn = method.getAnnotation(PermissionAction.class);
            var action = util.resolveResourceAction(perActAnn, handlerMethodEntry.getKey());
            var controllerManagedResource = controllerClass.getAnnotation(ControllerManagedResource.class);
            var resourceType = util.resolveResourceType(perActAnn, controllerManagedResource);
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
        var handlerMethods = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            var value = entry.getValue();
            if (method.equals(value.getMethod())) {
                return entry;
            }
        }
        return null;
    }

    private String resolveUrl(Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethodEntry, String requestUri) {
        Set<String> urls = handlerMethodEntry.getKey().getDirectPaths();
        String result = null;
        for (var url : urls) {
            if (url.equals(requestUri)) {
                result = url;
                break;
            } else if (result == null) {
                result = url;
            }
        }
        return result;
    }

    private boolean verifyRole(JwtTokenDto jwtTokenDto, UserRoleDto userRole, String action, String url,
                               RequestMethod requestMethod, String resourceType, ProceedingJoinPoint joinPoint) {
        var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
        List<? extends Permission> permissions = permissionRepository.findAllByRoleIdAndResourceType(userRole.getRoleId(), resourceType, sort);
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
        var anns = method.getParameterAnnotations();
        LOGGER.debug("ANN [{}]", new Object[] { anns });
        PermissionResource permissionResource = null;
        Object parameterValue = null;
        mainloop:
        for (int i = 0; i < anns.length; i++) {
            for (var annotation : anns[i]) {
                if (PermissionResource.class.isAssignableFrom(annotation.annotationType())) {
                    permissionResource = (PermissionResource) annotation;
                    parameterValue = joinPoint.getArgs()[i];
                    break mainloop;
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
            var extractedValue = util.getDeepAttributeValue(parameterValue, fieldPath);
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
