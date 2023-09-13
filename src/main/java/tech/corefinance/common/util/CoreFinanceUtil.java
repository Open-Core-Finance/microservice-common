package tech.corefinance.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.dto.SimpleVersion;
import tech.corefinance.common.dto.SimpleVersionComparator;
import tech.corefinance.common.context.ApplicationContextHolder;
import tech.corefinance.common.converter.ExportTypeConverter;
import tech.corefinance.common.ex.ReflectiveIncorrectFieldException;
import tech.corefinance.common.model.ResourceAction;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class CoreFinanceUtil {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String PARSING_JSON_FAILURE = "Parsing json failure";
    private static final String PARSING_JSON_FAILURE_LOG = "Parsing json failure! object: {}, error: {}";
    private static final List<Class<?>> LIST_IGNORE_LOGGING = List.of(ServletRequest.class, ServletResponse.class, HttpSession.class,
            Servlet.class, MultipartFile.class, byte[].class, File.class, InputStream.class);

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;
    @Autowired
    private SimpleVersionComparator simpleVersionComparator;

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
                logs.put(name, arg == null ? "null" : "<" + arg.getClass().getName() + "/>");
            }
        }
        return writeValueToJson(objectMapper, logs);
    }

    public String writeValueToJson(ObjectMapper objectMapper, Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (StackOverflowError | JsonProcessingException e) {
            logger.info(PARSING_JSON_FAILURE_LOG, object, e);
            return PARSING_JSON_FAILURE;
        }
    }

    public List<Resource> getResources(String regex, String nameSeparator, String versionSeparator) throws IOException {
        Predicate<? super Resource> fileNameFilter = r -> {
            String fileName = r.getFilename();
            if (fileName.contains(String.valueOf(nameSeparator))) {
                try {
                    convertVersion(fileName, nameSeparator, versionSeparator);
                    return true;
                } catch (IndexOutOfBoundsException | NumberFormatException | NullPointerException e) {
                    logger.error("Error", e);
                }
            }
            logger.debug("Ignore [{}] because file does not meet defined rule!", fileName);
            return false;
        };
        Comparator<Resource> fileNameComparator = (r1, r2) -> {
            SimpleVersion version1 = convertVersion(r1.getFilename(), nameSeparator, versionSeparator);
            SimpleVersion version2 = convertVersion(r2.getFilename(), nameSeparator, versionSeparator);
            int result = simpleVersionComparator.compare(version1, version2);
            if (result == 0) {
                result = r1.getFilename().compareTo(r2.getFilename());
            }
            return result;
        };
        return List.of(resourcePatternResolver.getResources(regex)).stream().filter(fileNameFilter).sorted(fileNameComparator)
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
                resourceType = ResourceAction.COMMON_ACTION_LIST;
            } else if (requestMethods.contains(RequestMethod.POST)) {
                resourceType = ResourceAction.COMMON_ACTION_ADD;
            } else if (requestMethods.contains(RequestMethod.PUT) || requestMethods.contains(RequestMethod.PATCH)) {
                resourceType = ResourceAction.COMMON_ACTION_UPDATE;
            } else {
                resourceType = ResourceAction.COMMON_ACTION_VIEW;
            }
        }
        return resourceType;
    }

    public String resolveResourceType(PermissionAction perActAnn, ControllerManagedResource managedResource) {
        var resourceType = perActAnn != null ? perActAnn.resourceType() : null;
        if (!StringUtils.hasText(resourceType)) {
            if (managedResource == null) {
                logger.error("Must define resource type at PermissionAction in method level " +
                        "or ControllerManagedResource in controller level.");
                throw new ReflectiveIncorrectFieldException("no_permission_defined");
            }
            resourceType = managedResource.value();
        }
        return resourceType;
    }

    public Object getDeepAttributeValue(Object object, String deepAttributePath) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(object);
        String[] attributeNames = deepAttributePath.split("\\.");

        for (var i = 0; i < attributeNames.length; i++) {
            var attributeName = attributeNames[i];
            object = beanWrapper.getPropertyValue(attributeName);
            if (object == null) {
                return null;
            }
            if (i < attributeNames.length) {
                beanWrapper = new BeanWrapperImpl(object);
            }
        }

        return object;
    }
}