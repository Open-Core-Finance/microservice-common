package tech.corefinance.common.services;

import tech.corefinance.common.config.ServiceSecurityConfig;
import tech.corefinance.common.controller.CommonController;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.repository.ResourceActionRepository;
import tech.corefinance.common.service.ControllerScanner;
import tech.corefinance.common.service.PermissionService;
import tech.corefinance.common.test.support.controllers.AnotherTestController;
import tech.corefinance.common.test.support.controllers.TestController;
import tech.corefinance.common.test.support.model.PermissionTest;
import tech.corefinance.common.util.Util;
import tech.corefinance.common.ex.ReflectiveIncorrectFieldException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class ControllerScannerTest {

    private ResourceActionRepository resourceActionRepository;
    private ServiceSecurityConfig serviceSecurityConfig;
    private List<String> listIgnoredPackage;
    private List<String> noAuthenUrls;
    private ControllerScanner controllerScanner;
    private RequestMappingHandlerMapping mapping;
    private Util util;
    private PermissionService permissionService;

    @BeforeEach
    public void setUp() throws IllegalAccessException {
        controllerScanner = new ControllerScanner();
        // ResourceActionRepository
        resourceActionRepository = PowerMockito.mock(ResourceActionRepository.class);
        var resourceActionRepositoryField = PowerMockito.field(ControllerScanner.class, "resourceActionRepository");
        resourceActionRepositoryField.setAccessible(true);
        resourceActionRepositoryField.set(controllerScanner, resourceActionRepository);
        // ServiceSecurityConfig
        serviceSecurityConfig = new ServiceSecurityConfig();
        listIgnoredPackage = new LinkedList<>();
        noAuthenUrls = new LinkedList<>();
        serviceSecurityConfig.setIgnoreControllerScan(listIgnoredPackage);
        serviceSecurityConfig.setNoAuthenUrls(noAuthenUrls);
        var serviceSecurityConfigField = PowerMockito.field(ControllerScanner.class, "serviceSecurityConfig");
        serviceSecurityConfigField.setAccessible(true);
        serviceSecurityConfigField.set(controllerScanner, serviceSecurityConfig);
        // RequestMappingHandlerMapping
        mapping = PowerMockito.mock(RequestMappingHandlerMapping.class);
        var mappingField = PowerMockito.field(ControllerScanner.class, "mapping");
        mappingField.setAccessible(true);
        mappingField.set(controllerScanner, mapping);
        // Util
        util = new Util();
        var utilField = PowerMockito.field(ControllerScanner.class, "util");
        utilField.setAccessible(true);
        utilField.set(controllerScanner, util);
        // Permission service
        permissionService = PowerMockito.mock(PermissionService.class);
        var permissionServiceField = PowerMockito.field(ControllerScanner.class, "permissionService");
        permissionServiceField.setAccessible(true);
        permissionServiceField.set(controllerScanner, permissionService);
        PowerMockito.when(permissionService.newPermission()).thenReturn(new PermissionTest());
    }

    @Test
    public void test_scan_ignoredPackage() throws NoSuchMethodException {
        // Handlers
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = new HashMap<>();
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths("/test/normal").build();
        Method method = TestController.class.getDeclaredMethod("normal");
        var testController = new TestController();
        HandlerMethod handlerMethod = new HandlerMethod(testController, method);
        handlerMethods.put(requestMappingInfo, handlerMethod);
        PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
        // Config
        listIgnoredPackage.add(".test.");
        // Check if call save with empty list
        var result = new LinkedList<ResourceAction>();
        controllerScanner.scan();
        // Verify
        verify(resourceActionRepository, times(1)).saveAll(result);
    }

    @Test
    public void test_scan_noConfiguredController() throws NoSuchMethodException {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = new HashMap<>();
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths("/test/api/jsonerror").build();
        Method method = TestController.class.getDeclaredMethod("jsonerror");
        var testController = new TestController();
        HandlerMethod handlerMethod = new HandlerMethod(testController, method);
        handlerMethods.put(requestMappingInfo, handlerMethod);
        PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
        // Invoke and got exception
        assertThrowsExactly(ReflectiveIncorrectFieldException.class, () -> controllerScanner.scan());
        // Verify if do not call save empty list
        var result = new LinkedList<ResourceAction>();
        verify(resourceActionRepository, times(0)).saveAll(result);
    }

    @Test
    public void test_scan_noAuthenUrl() throws NoSuchMethodException {
        // Handlers
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = new HashMap<>();
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths("/test-api").build();
        Method method = TestController.class.getDeclaredMethod("normal");
        var testController = new TestController();
        HandlerMethod handlerMethod = new HandlerMethod(testController, method);
        handlerMethods.put(requestMappingInfo, handlerMethod);
        PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
        // Config
        noAuthenUrls.add("/test-api");
        // Check if call save with empty list
        var result = new LinkedList<ResourceAction>();
        controllerScanner.scan();
        // Verify
        verify(resourceActionRepository, times(1)).saveAll(result);
    }

    @Test
    public void test_scan_noAuthenUrlWithWildcard() throws NoSuchMethodException {
        // Handlers
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = new HashMap<>();
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths("/test-api").build();
        Method method = TestController.class.getDeclaredMethod("normal");
        var testController = new TestController();
        HandlerMethod handlerMethod = new HandlerMethod(testController, method);
        handlerMethods.put(requestMappingInfo, handlerMethod);
        PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
        // Config
        noAuthenUrls.add("/test-*");
        // Check if call save with empty list
        var result = new LinkedList<ResourceAction>();
        controllerScanner.scan();
        // Verify
        verify(resourceActionRepository, times(1)).saveAll(result);
    }

    @Test
    public void test_scan_manualOnly() throws NoSuchMethodException {
        // Handlers
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = new HashMap<>();
        String url = "/test/another-normal";
        RequestMethod requestMethod = RequestMethod.POST;
        RequestMappingInfo requestMappingInfo =
                RequestMappingInfo.paths(url).methods(requestMethod).build();
        Method method = AnotherTestController.class.getDeclaredMethod("anotherNormal", HttpServletRequest.class,
                HttpServletResponse.class);
        var testController = new AnotherTestController();
        HandlerMethod handlerMethod = new HandlerMethod(testController, method);
        handlerMethods.put(requestMappingInfo, handlerMethod);
        PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
        // Config
        noAuthenUrls.add("/abc/*");
        // Call
        controllerScanner.scan();
        // Verify
        var result = new LinkedList<ResourceAction>();
        var resourceAction = new ResourceAction("test-common", "add", url, requestMethod);
        resourceAction.setId("add-test-common-_test_another-normal");
        result.add(resourceAction);
        verify(permissionService, times(1)).saveOrUpdatePermission(Mockito.any());
        verify(resourceActionRepository, times(1)).saveAll(result);
    }

    @Test
    public void test_scan_ConfiguredActionAndResource() throws NoSuchMethodException {
        // Handlers
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = new HashMap<>();
        String url = "/common/initialization-default-permissions-data";
        var requestMethod = RequestMethod.GET;
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(url).methods(requestMethod).build();
        Method method = CommonController.class.getDeclaredMethod("initializationDefaultData");
        var testController = new CommonController();
        HandlerMethod handlerMethod = new HandlerMethod(testController, method);
        handlerMethods.put(requestMappingInfo, handlerMethod);
        PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
        // Check saved empty list by checking exception
        var result = new LinkedList<ResourceAction>();
        var resourceAction = new ResourceAction("common", "initial", url, requestMethod);
        resourceAction.setId("initial-common-_common_initialization-default-permissions-data");
        result.add(resourceAction);
        controllerScanner.scan();
        // Verify
        verify(resourceActionRepository, times(1)).saveAll(result);
    }
}