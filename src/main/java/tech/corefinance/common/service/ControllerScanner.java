package tech.corefinance.common.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.annotation.ManualPermissionCheck;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.config.ServiceSecurityConfig;
import tech.corefinance.common.dto.PermissionDto;
import tech.corefinance.common.enums.AccessControl;
import tech.corefinance.common.ex.ReflectiveIncorrectFieldException;
import tech.corefinance.common.model.Permission;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.repository.ResourceActionRepository;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "scan-controllers-actions", havingValue = "true",
        matchIfMissing = true)
@Slf4j
public class ControllerScanner {

    @Autowired
    private ResourceActionRepository resourceActionRepository;
    @Autowired
    private ServiceSecurityConfig serviceSecurityConfig;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private List<RequestMappingHandlerMapping> handlerMappings;
    @Autowired
    private CoreFinanceUtil coreFinanceUtil;

    @Async
    @PostConstruct
    public void scan() {
        for (var mapping : handlerMappings) {
            scanHandler(mapping);
        }
    }

    private void scanHandler(RequestMappingHandlerMapping mapping) {
        var handlerMethods = mapping.getHandlerMethods();
        var permissionActions = new LinkedList<ResourceAction>();
        main_loop:
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            var urls = key.getDirectPaths();
            Method method = value.getMethod();
            var declaringClass = method.getDeclaringClass();
            var controllerClass = value.getBeanType();
            String controllerClassName = controllerClass.getName();
            log.debug("{}#{}", controllerClassName, method.getName());
            for (String ignoreController : serviceSecurityConfig.getIgnoreControllerScan()) {
                log.debug("Checking if controller full package contain [{}] or not...", ignoreController);
                if (controllerClassName.contains(ignoreController)) {
                    log.debug("Skipped permission scan for {}", declaringClass);
                    continue main_loop;
                }
            }
            for (String url : urls) {
                log.debug("Validating URL [{}]", url);
                for (String noAuthenUrl : serviceSecurityConfig.getNoAuthenUrls()) {
                    Pattern pattern = Pattern.compile(noAuthenUrl.replace("*", ".*"));
                    var matched = pattern.matcher(url).matches();
                    log.debug("Checking result with pattern [{}] is [{}]", noAuthenUrl, matched);
                    if (matched) {
                        continue main_loop;
                    }
                }
            }
            var controllerManagedResource = controllerClass.getAnnotation(ControllerManagedResource.class);
            var perActAnn = method.getAnnotation(PermissionAction.class);
            var manualPerCheckAnn = method.getAnnotation(ManualPermissionCheck.class);
            if (perActAnn == null) {
                if (controllerManagedResource == null) {
                    log.error("{}={} have no annotation PermissionAction!", declaringClass.getName(),
                            method.getName());
                    throw new ReflectiveIncorrectFieldException("no_permission_defined");
                }
            }
            var resourceType = coreFinanceUtil.resolveResourceType(perActAnn, controllerManagedResource);
            var action = coreFinanceUtil.resolveResourceAction(perActAnn, key);
            var requestMethods = key.getMethodsCondition().getMethods();
            permissionActions.addAll(buildListActions(resourceType, action, urls, requestMethods));
            if (manualPerCheckAnn != null) {
                saveManualCheckPermissions(resourceType, action, urls, requestMethods);
            }
        }
        log.info("{}", permissionActions);
        resourceActionRepository.saveAll(permissionActions);
    }

    private List<ResourceAction> buildListActions(String resourceType, String action, Iterable<String> urls,
                                                  Iterable<RequestMethod> requestMethods) {
        var permissionActions = new LinkedList<ResourceAction>();
        for (String url : urls) {
            for (RequestMethod requestMethod : requestMethods) {
                permissionActions.add(permissionService.newResourceAction(resourceType, action, url, requestMethod));
            }
        }
        return permissionActions;
    }

    private void saveManualCheckPermissions(String resourceType, String action, Iterable<String> urls,
                                            Iterable<RequestMethod> requestMethods) {
        for (String url : urls) {
            for (RequestMethod requestMethod : requestMethods) {
                var permission = new PermissionDto();
                permission.setControl(AccessControl.MANUAL_CHECK);
                permission.setUrl(url);
                permission.setRoleId(Permission.ANY_ROLE_APPLIED_VALUE);
                permission.setResourceType(resourceType);
                permission.setAction(action);
                permission.setRequestMethod(requestMethod);
                permissionService.createOrUpdateEntity(permission);
            }
        }
    }
}
