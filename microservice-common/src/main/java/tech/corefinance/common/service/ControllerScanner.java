package tech.corefinance.common.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.util.Pair;
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
import tech.corefinance.common.entity_author.Permission;
import tech.corefinance.common.enums.AccessControl;
import tech.corefinance.common.ex.ReflectiveIncorrectFieldException;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.repository.ResourceActionRepository;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "scan-controllers-actions", havingValue = "true", matchIfMissing = true)
@Slf4j
public class ControllerScanner {

    @Autowired
    private ResourceActionRepository resourceActionRepository;
    @Autowired
    private ServiceSecurityConfig serviceSecurityConfig;
    @Autowired(required = false)
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
        main_loop:
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            var urls = coreFinanceUtil.buildUrlPair(key.getPatternValues());
            Method method = value.getMethod();
            var declaringClass = method.getDeclaringClass();
            var controllerClass = value.getBeanType();
            String controllerClassName = controllerClass.getName();
            log.debug("{}#{} ==> {}", controllerClassName, method.getName(), urls);
            for (String ignoreController : serviceSecurityConfig.getIgnoreControllerScan()) {
                log.debug("Checking if controller full package contain [{}] or not...", ignoreController);
                if (controllerClassName.contains(ignoreController)) {
                    log.debug("Skipped permission scan for {}", declaringClass);
                    continue main_loop;
                }
            }
            log.debug("URLs {}", urls);
            var controllerManagedResource = controllerClass.getAnnotation(ControllerManagedResource.class);
            var perActAnn = method.getAnnotation(PermissionAction.class);
            var manualPerCheckAnn = method.getAnnotation(ManualPermissionCheck.class);
            String description = "";
            main_for:
            for (var url : urls) {
                boolean noAuthenRequired = false;
                log.debug("Validating URL [{}]", url);
                for (String noAuthenUrl : serviceSecurityConfig.getNoAuthenUrls()) {
                    Pattern pattern = Pattern.compile(noAuthenUrl.replace("*", ".*"));
                    var matched = pattern.matcher(url.getSecond()).matches();
                    log.debug("Checking result with pattern [{}] is [{}]", noAuthenUrl, matched);
                    if (matched) {
                        if (serviceSecurityConfig.isRequireControllerWithAnnotation()) {
                            log.debug("Skipped!! Required annotation [true]!!");
                            continue main_for;
                        } else {
                            log.debug("No skip!! Required annotation [false]!!");
                            noAuthenRequired = true;
                        }
                    }
                }
                if (perActAnn == null) {
                    if (controllerManagedResource == null) {
                        log.error("{}={} have no annotation PermissionAction!", declaringClass.getName(), method.getName());
                        if (serviceSecurityConfig.isRequireControllerWithAnnotation()) {
                            throw new ReflectiveIncorrectFieldException("no_permission_defined");
                        }
                    }
                } else {
                    description = perActAnn.description();
                }
                if (description.isEmpty()) {
                    description = coreFinanceUtil.resolveDefaultDescription(perActAnn, key);
                }
                var resourceType = coreFinanceUtil.resolveResourceType(perActAnn, controllerManagedResource, serviceSecurityConfig);
                var action = coreFinanceUtil.resolveResourceAction(perActAnn, key);
                var requestMethods = key.getMethodsCondition().getMethods();
                if (manualPerCheckAnn != null) {
                    saveManualCheckPermission(resourceType, action, url, requestMethods);
                } else {
                    ResourceAction item = buildAction(resourceType, action, url, requestMethods, description, noAuthenRequired);
                    if (item != null) {
                        resourceActionRepository.save(item);
                    }
                }
            }
        }
    }

    private ResourceAction buildAction(String resourceType, String action, Pair<String, String> url,
                                       Iterable<RequestMethod> requestMethods, String description, boolean noAuthenRequired) {
        for (RequestMethod requestMethod : requestMethods) {
            log.debug("Checking URL [{}] with method [{}]", url, requestMethod);
            var resourceAction = newResourceAction(resourceType, action, url.getSecond(), requestMethod, description, noAuthenRequired, url.getFirst());
            if (!resourceActionRepository.existsByActionAndRequestMethodAndResourceTypeAndUrl(action, requestMethod, resourceType,
                    url.getSecond())) {
                log.debug("Saving action [{}]", resourceAction);
                return resourceActionRepository.save(resourceAction);
            } else {
                log.debug("Skip existed action [{}]", resourceAction);
            }
        }
        return null;
    }

    private void saveManualCheckPermission(String resourceType, String action, Pair<String, String> url,
                                           Iterable<RequestMethod> requestMethods) {
        for (RequestMethod requestMethod : requestMethods) {
            var permission = new PermissionDto();
            permission.setControl(AccessControl.MANUAL_CHECK);
            permission.setUrl(url.getSecond());
            permission.setRoleId(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setResourceType(resourceType);
            permission.setAction(action);
            permission.setRequestMethod(requestMethod);
            if (permissionService != null) {
                permissionService.createOrUpdateEntity(permission);
            }
        }
    }

    /**
     * Create ResourceAction.
     *
     * @param resourceType  Resource type name.
     * @param action        Action name.
     * @param url           URL.
     * @param requestMethod Request Method.
     * @return New ResourceAction object.
     */
    public @NotNull ResourceAction newResourceAction(String resourceType, String action, String url, RequestMethod requestMethod,
                                                     String description, boolean noAuthenRequired, String originUrl) {
        return new ResourceAction(resourceType, action, url, requestMethod, description, noAuthenRequired, originUrl);
    }
}
