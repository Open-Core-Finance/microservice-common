package com.finance.core.common.service;

import com.finance.core.common.config.ServiceSecurityConfig;
import com.finance.core.common.enums.AccessControl;
import com.finance.core.common.model.AbstractPermission;
import com.finance.core.common.model.ResourceAction;
import com.finance.core.common.util.Util;
import com.finance.core.common.annotation.ControllerManagedResource;
import com.finance.core.common.annotation.ManualPermissionCheck;
import com.finance.core.common.annotation.PermissionAction;
import com.finance.core.common.ex.ReflectiveIncorrectFieldException;
import com.finance.core.common.repository.ResourceActionRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@ConditionalOnProperty(prefix = "com.finance.core.security", name = "scan-controllers-actions", havingValue = "true", matchIfMissing = true)
public class ControllerScanner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResourceActionRepository resourceActionRepository;
    @Autowired
    private ServiceSecurityConfig serviceSecurityConfig;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RequestMappingHandlerMapping mapping;
    @Autowired
    private Util util;

    @Async
    @PostConstruct
    public void scan() {
        var handlerMethods = mapping.getHandlerMethods();
        var permissionActions = new LinkedList<ResourceAction>();
        mainloop:
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            var urls = key.getDirectPaths();
            Method method = value.getMethod();
            var controllerClass = method.getDeclaringClass();
            String controllerClassName = controllerClass.getName();
            logger.debug("{}#{}", controllerClassName, method.getName());
            for (String ignoreController : serviceSecurityConfig.getIgnoreControllerScan()) {
                logger.debug("Checking if controller full package contain [{}] or not...", ignoreController);
                if (controllerClassName.contains(ignoreController)) {
                    logger.debug("Skipped permission scan for {}", controllerClass);
                    continue mainloop;
                }
            }
            for (String url : urls) {
                logger.debug("Validating URL [{}]", url);
                for (String noAuthenUrl : serviceSecurityConfig.getNoAuthenUrls()) {
                    Pattern pattern = Pattern.compile(noAuthenUrl.replace("*", ".*"));
                    var matched = pattern.matcher(url).matches();
                    logger.debug("Checking result with pattern [{}] is [{}]", noAuthenUrl, matched);
                    if (matched) {
                        continue mainloop;
                    }
                }
            }
            var controllerManagedResource = controllerClass.getAnnotation(ControllerManagedResource.class);
            var perActAnn = method.getAnnotation(PermissionAction.class);
            var manualPerCheckAnn = method.getAnnotation(ManualPermissionCheck.class);
            if (perActAnn == null) {
                if (controllerManagedResource == null) {
                    logger.error("{}={} have no annotation PermissionAction!", controllerClass.getName(),
                            method.getName());
                    throw new ReflectiveIncorrectFieldException("no_permission_defined");
                }
            }
            var resourceType = util.resolveResourceType(perActAnn, controllerManagedResource);
            var action = util.resolveResourceAction(perActAnn, key);
            var requestMethods = key.getMethodsCondition().getMethods();
            permissionActions.addAll(buildListActions(resourceType, action, urls, requestMethods));
            if (manualPerCheckAnn != null) {
                saveManualCheckPermissions(resourceType, action, urls, requestMethods);
            }
        }
        logger.info("{}", permissionActions);
        resourceActionRepository.saveAll(permissionActions);
    }

    private List<ResourceAction> buildListActions(String resourceType, String action, Iterable<String> urls, Iterable<RequestMethod> requestMethods) {
        var permissionActions = new LinkedList<ResourceAction>();
        for (String url : urls) {
            for (RequestMethod requestMethod : requestMethods) {
                permissionActions.add(new ResourceAction(resourceType, action, url, requestMethod));
            }
        }
        return permissionActions;
    }

    private void saveManualCheckPermissions(String resourceType, String action, Iterable<String> urls, Iterable<RequestMethod> requestMethods) {
        for (String url : urls) {
            for (RequestMethod requestMethod : requestMethods) {
                var permission = new AbstractPermission();
                permission.setControl(AccessControl.MANUAL_CHECK);
                permission.setUrl(url);
                permission.setRoleId(AbstractPermission.ANY_ROLE_APPLIED_VALUE);
                permission.setResourceType(resourceType);
                permission.setAction(action);
                permission.setRequestMethod(requestMethod);
                permissionService.saveOrUpdatePermission(permission);
            }
        }
    }
}
