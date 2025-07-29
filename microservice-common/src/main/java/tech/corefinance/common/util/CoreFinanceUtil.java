package tech.corefinance.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.config.ServiceSecurityConfig;
import tech.corefinance.common.context.ApplicationContextHolder;
import tech.corefinance.common.converter.ExportTypeConverter;
import tech.corefinance.common.dto.SimpleVersion;
import tech.corefinance.common.dto.SimpleVersionComparator;
import tech.corefinance.common.ex.ReflectiveIncorrectFieldException;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.common.service.ProxyUnbox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class CoreFinanceUtil {

    public static final String PARSING_JSON_FAILURE = "Parsing json failure";
    private static final String PARSING_JSON_FAILURE_LOG = "Parsing json failure! object: {}";
    private static final List<Class<?>> LIST_IGNORE_LOGGING =
            List.of(ServletRequest.class, ServletResponse.class, HttpSession.class, Servlet.class, MultipartFile.class, byte[].class,
                    File.class, InputStream.class, Class.class, Method.class, Field.class);

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;
    @Autowired
    private SimpleVersionComparator simpleVersionComparator;
    @Autowired
    private List<ProxyUnbox> proxyUnboxes;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object checkAndConvertExportData(Object data) {
        if (data == null) {
            return null;
        }
        ApplicationContextHolder contextHolder = ApplicationContextHolder.getInstance();
        Map<String, ExportTypeConverter> beans = contextHolder.getApplicationContext().getBeansOfType(ExportTypeConverter.class);
        for (Map.Entry<String, ExportTypeConverter> entry : beans.entrySet()) {
            ExportTypeConverter converter = entry.getValue();
            if (converter.isSupport(data.getClass())) {
                data = converter.convert(data);
                break;
            }
        }
        return data;
    }

    public String buildMethodInputJsonLog(ProceedingJoinPoint joinPoint, String[] parametersNames, ObjectMapper objectMapper) {
        // Create AopCustomParam
        Map<String, Object> logs = new LinkedHashMap<>();
        Object[] args = joinPoint.getArgs();

        if (parametersNames != null) {
            for (int i = 0; i < parametersNames.length; i++) {
                String name = parametersNames[i];
                Object arg = args[i];
                boolean shouldIgnore = LIST_IGNORE_LOGGING.stream().anyMatch(clzz -> arg != null && clzz.isAssignableFrom(arg.getClass()));
                if (!shouldIgnore && arg != null) {
                    shouldIgnore = arg.getClass().getSimpleName().contains("$");
                }
                if (!shouldIgnore) {
                    logs.put(name, writeValueToJson(objectMapper, arg));
                } else {
                    logs.put(name, "<" + arg.getClass().getName() + "/>");
                }
            }
        }
        return writeValueToJson(objectMapper, logs);
    }

    public String writeValueToJson(ObjectMapper objectMapper, Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (StackOverflowError | JsonProcessingException e) {
            log.info(PARSING_JSON_FAILURE_LOG, object, e);
            return PARSING_JSON_FAILURE;
        }
    }

    public List<Resource> getResources(String regex, String nameSeparator, String versionSeparator) throws IOException {
        Predicate<? super Resource> fileNameFilter = r -> {
            String fileName = r.getFilename();
            assert fileName != null;
            if (fileName.contains(String.valueOf(nameSeparator))) {
                try {
                    convertVersion(fileName, nameSeparator, versionSeparator);
                    return true;
                } catch (IndexOutOfBoundsException | NumberFormatException | NullPointerException e) {
                    log.error("Error", e);
                }
            }
            log.debug("Ignore [{}] because file does not meet defined rule!", fileName);
            return false;
        };
        Comparator<Resource> fileNameComparator = (r1, r2) -> {
            SimpleVersion version1 = convertVersion(r1.getFilename(), nameSeparator, versionSeparator);
            SimpleVersion version2 = convertVersion(r2.getFilename(), nameSeparator, versionSeparator);
            int result = simpleVersionComparator.compare(version1, version2);
            if (result == 0) {
                result = r1.getFilename().compareTo(Objects.requireNonNull(r2.getFilename()));
            }
            return result;
        };
        return Stream.of(resourcePatternResolver.getResources(regex)).filter(fileNameFilter).sorted(fileNameComparator)
                .collect(Collectors.toList());
    }

    public SimpleVersion convertVersion(String fileName, String nameSeparator, String versionSeparator) {
        String versionPart = fileName != null ? fileName : "0.1";
        if (versionPart.contains(String.valueOf(nameSeparator))) {
            versionPart = versionPart.substring(0, versionPart.indexOf(nameSeparator));
        }
        short major = Short.parseShort(versionPart.substring(0, versionPart.indexOf(versionSeparator)));
        short minor = Short.parseShort(versionPart.substring(versionPart.indexOf(versionSeparator) + 1));
        return new SimpleVersion(major, minor);
    }

    public String resolveResourceAction(PermissionAction perActAnn, RequestMappingInfo requestMappingInfo) {
        var resourceType = perActAnn != null ? perActAnn.action() : null;
        if (!StringUtils.hasText(resourceType)) {
            var requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
            if (requestMethods.contains(RequestMethod.DELETE)) {
                resourceType = ResourceAction.COMMON_ACTION_DELETE;
            } else if (requestMethods.contains(RequestMethod.GET)) {
                resourceType = ResourceAction.COMMON_ACTION_VIEW;
            } else if (requestMethods.contains(RequestMethod.POST)) {
                resourceType = ResourceAction.COMMON_ACTION_ADD;
            } else if (requestMethods.contains(RequestMethod.PUT) || requestMethods.contains(RequestMethod.PATCH)) {
                resourceType = ResourceAction.COMMON_ACTION_UPDATE;
            } else {
                resourceType = ResourceAction.COMMON_ACTION_LIST;
            }
        }
        return resourceType;
    }

    public String resolveDefaultDescription(PermissionAction perActAnn, RequestMappingInfo requestMappingInfo) {
        var resourceType = perActAnn != null ? perActAnn.action() : null;
        if (!StringUtils.hasText(resourceType)) {
            var requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
            if (requestMethods.contains(RequestMethod.DELETE)) {
                resourceType = ResourceAction.COMMON_DESCRIPTION_DELETE;
            } else if (requestMethods.contains(RequestMethod.GET)) {
                resourceType = ResourceAction.COMMON_DESCRIPTION_VIEW;
            } else if (requestMethods.contains(RequestMethod.POST)) {
                resourceType = ResourceAction.COMMON_DESCRIPTION_ADD;
            } else if (requestMethods.contains(RequestMethod.PUT) || requestMethods.contains(RequestMethod.PATCH)) {
                resourceType = ResourceAction.COMMON_DESCRIPTION_UPDATE;
            } else {
                resourceType = ResourceAction.COMMON_DESCRIPTION_LIST;
            }
        }
        return resourceType;
    }

    public String resolveResourceType(PermissionAction perActAnn, ControllerManagedResource managedResource,
            ServiceSecurityConfig serviceSecurityConfig) {
        var resourceType = perActAnn != null ? perActAnn.resourceType() : null;
        if (!StringUtils.hasText(resourceType)) {
            if (managedResource == null) {
                log.error(
                        "Must define resource type at PermissionAction in method level " + "or ControllerManagedResource in controller level.");
                if (serviceSecurityConfig.isRequireControllerWithAnnotation()) {
                    throw new ReflectiveIncorrectFieldException("no_permission_defined");
                }
                resourceType = "no_configured_resource";
            } else {
                resourceType = managedResource.value();
            }
        }
        return resourceType;
    }

    public Object getDeepAttributeValue(Object object, String deepAttributePath) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(object);
        String[] attributeNames = deepAttributePath.split("\\.");

        log.trace("Object [{}] and attribute \"{}\" as {}", object, deepAttributePath, Arrays.toString(attributeNames));
        for (String attributeName : attributeNames) {
            object = beanWrapper.getPropertyValue(attributeName);
            log.trace("Got value [{}] for attribute [{}]", object, attributeName);
            if (object == null) {
                return null;
            }
            beanWrapper = new BeanWrapperImpl(object);
        }
        log.trace("Return attribute value... [{}]", object);
        return object;
    }

    public Class<?> findEntityTypeFromCommonService(Class<?> serviceClass) {
        log.debug("Finding entity type for service [{}]", serviceClass.getName());
        Type superClass = serviceClass.getGenericSuperclass();
        log.debug("Supper class [{}]", superClass);
        if (superClass instanceof ParameterizedType) {
            return extractGenericEntityType((ParameterizedType) superClass);
        } else {
            Type[] interfaces = serviceClass.getGenericInterfaces();
            log.debug("Continue with support interfaces {}", Arrays.toString(interfaces));
            for (var type : interfaces) {
                if (type instanceof ParameterizedType) {
                    log.debug("Type [{}] is ParameterizedType!", type);
                    if (CommonService.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType())) {
                        log.debug("Extracting entity type...");
                        return extractGenericEntityType((ParameterizedType) type);
                    } else {
                        log.debug("Ignored [{}] because it's not CommonService!", type);
                    }
                } else {
                    if (type instanceof Class<?>) {
                        log.debug("Type [{}] is Class. Continue with findEntityTypeFromCommonService...", type);
                        var tmp = findEntityTypeFromCommonService((Class<?>) type);
                        if (tmp != null) {
                            return tmp;
                        }
                    } else {
                        log.debug("Ignored unknown type [{}]!", type);
                    }
                }
            }
        }
        return null;
    }

    private Class<?> extractGenericEntityType(ParameterizedType type) {
        var argumentTypes = type.getActualTypeArguments();
        log.debug("Actual argument type {}", Arrays.toString(argumentTypes));
        for (var t : argumentTypes) {
            if (t instanceof Class<?> && GenericModel.class.isAssignableFrom((Class<?>) t)) {
                return (Class<?>) t;
            }
        }
        return null;
    }

    /**
     * Check if input target is proxy object or not. And check if it can cast to one of expected target classes.
     *
     * @param target         target
     * @param excludeClasses Target classes to check
     * @return True if proxyObject can cast to one of expected target classes.
     */
    public boolean isMatchedInstanceType(Object target, List<String> excludeClasses) {
        var result = false;
        log.debug("Checking if [{}] is included in list {}", target, excludeClasses);
        for (String className : excludeClasses) {
            try {
                var clzz = Class.forName(className);
                var targetClass = target.getClass();
                if (clzz.isAssignableFrom(targetClass)) {
                    log.debug("[{}] can assign from [{}]", clzz, targetClass);
                    return true;
                } else {
                    log.debug("[{}] can not assign from [{}]", clzz, targetClass);
                }
            } catch (ClassNotFoundException e) {
                log.error("Invalid configuration for class name [{}]", className, e);
            }
        }
        return result;
    }

    public Object unProxy(Object proxyObject) {
        for (var unbox : proxyUnboxes) {
            if (unbox.canUnbox(proxyObject)) {
                return unbox.unProxy(proxyObject);
            }
        }
        return null;
    }

    public Set<Pair<String, String>> buildUrlPair(Set<String> urlPatterns) {
        Set<Pair<String, String>> urlPairs = new HashSet<>();
        urlPatterns.forEach(pattern -> urlPairs.add(Pair.of(pattern, patternToUrl(pattern))));
        return urlPairs;
    }

    private String patternToUrl(String pattern) {
        var url = pattern;
        var lastOpenCurlyBracketIndex = pattern.lastIndexOf("{");
        var firstCloseCurlyBracketIndex = pattern.indexOf("}");
        while (lastOpenCurlyBracketIndex >= 0 && lastOpenCurlyBracketIndex < firstCloseCurlyBracketIndex) {
            url = url.substring(0, lastOpenCurlyBracketIndex) + '*' + url.substring(firstCloseCurlyBracketIndex + 1);
            lastOpenCurlyBracketIndex = url.lastIndexOf("{");
            firstCloseCurlyBracketIndex = url.indexOf("}");
        }
        return url;
    }

    public AccessibleObject findAnnotatedField(Object obj, Class<?> objClass, Class<? extends Annotation> annotationClass)
            throws NoSuchFieldException {
        return findAnnotatedFieldOrName(obj, objClass, annotationClass, null);
    }

    public AccessibleObject findAnnotatedFieldOrName(Object obj, Class<?> objClass, Class<? extends Annotation> annotationClass,
            String fieldName) throws NoSuchFieldException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(objClass);
        for (var method : methods) {
            var createdByAnn = method.getAnnotation(annotationClass);
            if (createdByAnn != null) {
                return method;
            }
        }
        Field[] fields = objClass.getDeclaredFields();
        for (var field : fields) {
            var createdByAnn = field.getAnnotation(CreatedBy.class);
            if (createdByAnn != null) {
                return field;
            }
        }
        return accessField(obj, objClass, fieldName);
    }

    public Method findGetterBySetter(Object obj, Class<?> objClass, Method setter) throws NoSuchMethodException {
        var setterName = setter.getName();
        if (setterName.startsWith("set")) {
            var getterName = setterName.replace("set", "get");
            return objClass.getDeclaredMethod(getterName);
        }
        return null;
    }

    public void triggerSetFieldValue(AccessibleObject accessibleObject, Object obj, Class<?> objClass, Object value)
            throws ReflectiveOperationException {
        if (accessibleObject instanceof Field f) {
            f.set(obj, value);
        } else if (accessibleObject instanceof Method m) {
            m.invoke(obj, value);
        } else {
            throw new NoSuchFieldException();
        }
    }

    public AccessibleObject accessField(Object obj, Class<?> objClass, String fieldName) throws NoSuchFieldException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(objClass);
        if (StringUtils.hasText(fieldName)) {
            String setMethodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            for (var method : methods) {
                if (method.getName().equalsIgnoreCase(setMethodName)) {
                    return method;
                }
            }
            return objClass.getField(fieldName);
        }
        return null;
    }

    public Object triggerGetFieldValue(AccessibleObject accessibleObject, Object obj, Class<?> objClass)
            throws ReflectiveOperationException {
        if (accessibleObject instanceof Field f) {
            return f.get(obj);
        } else if (accessibleObject instanceof Method m) {
            return m.invoke(obj);
        } else {
            throw new NoSuchFieldException();
        }
    }
}
