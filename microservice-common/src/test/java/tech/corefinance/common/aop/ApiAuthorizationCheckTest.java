package tech.corefinance.common.aop;

import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.controller.PermissionController;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.dto.UserRoleDto;
import tech.corefinance.common.entity_author.Permission;
import tech.corefinance.common.enums.AccessControl;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.repository.PermissionRepository;
import tech.corefinance.common.service.ResourceOwnerVerifier;
import tech.corefinance.common.test.support.controllers.AnotherTestController;
import tech.corefinance.common.test.support.controllers.TestController;
import tech.corefinance.common.test.support.model.UserTest;
import tech.corefinance.common.test.support.model.UserWrapterTest;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ApiAuthorizationCheckTest {

    private ApiAuthorizationCheck apiAuthorizationCheck;
    private MockHttpServletRequest request;
    private RequestMappingHandlerMapping mapping;
    private CoreFinanceUtil coreFinanceUtil;
    private PermissionRepository permissionRepository;
    private List<ResourceOwnerVerifier> resourceOwnerVerifiers;
    private AccessControl permissionDefaultControl;

    private ProceedingJoinPoint joinPoint;
    private ProxyMethodInvocation methodInvocation;

    @BeforeEach
    public void setUp() throws Throwable {
        apiAuthorizationCheck = new ApiAuthorizationCheck();
        // HttpServletRequest
        request = new MockHttpServletRequest();
        var field = PowerMockito.field(ApiAuthorizationCheck.class, "request");
        field.setAccessible(true);
        field.set(apiAuthorizationCheck, request);
        // mapping
        mapping = PowerMockito.mock(RequestMappingHandlerMapping.class);
        field = PowerMockito.field(ApiAuthorizationCheck.class, "handlerMappings");
        field.setAccessible(true);
        field.set(apiAuthorizationCheck, List.of(mapping));
        // Util
        coreFinanceUtil = new CoreFinanceUtil();
        field = PowerMockito.field(ApiAuthorizationCheck.class, "coreFinanceUtil");
        field.setAccessible(true);
        field.set(apiAuthorizationCheck, coreFinanceUtil);
        // permissionRepository
        permissionRepository = PowerMockito.mock(PermissionRepository.class);
        field = PowerMockito.field(ApiAuthorizationCheck.class, "permissionRepository");
        field.setAccessible(true);
        field.set(apiAuthorizationCheck, permissionRepository);
        // resourceOwnerVerifiers
        resourceOwnerVerifiers = new LinkedList<>();
        field = PowerMockito.field(ApiAuthorizationCheck.class, "resourceOwnerVerifiers");
        field.setAccessible(true);
        field.set(apiAuthorizationCheck, resourceOwnerVerifiers);
        // permissionDefaultControl
        permissionDefaultControl = AccessControl.DENIED;
        field = PowerMockito.field(ApiAuthorizationCheck.class, "permissionDefaultControl");
        field.setAccessible(true);
        field.set(apiAuthorizationCheck, permissionDefaultControl);
        // joinPoint
        methodInvocation = PowerMockito.mock(ProxyMethodInvocation.class);
        joinPoint = new MethodInvocationProceedingJoinPoint(methodInvocation);
        PowerMockito.when(methodInvocation.invocableClone()).thenReturn(methodInvocation);
    }

    @Test
    public void test_verifyGetRequest_noAuthenToken() throws Throwable {
        // Invoke
        PowerMockito.when(methodInvocation.proceed()).thenReturn(new Object());
        apiAuthorizationCheck.verifyGetRequest(joinPoint);
        // Verify
        verify(methodInvocation, times(1)).proceed();
        verify(methodInvocation, times(0)).getThis();
    }

    @Test
    public void test_verifyPostRequest_noHandler() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDtos.add(userRoleDto);
        userRoleDto.setRoleId("aaa");
        userRoleDto.setResourceType("common");
        userRoleDto.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new PermissionController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Invoke
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(new LinkedList<Permission>());
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);
            assertThrowsExactly(IllegalStateException.class, () -> apiAuthorizationCheck.verifyPostRequest(joinPoint));
        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyPutRequest_HandlerMethodNotMatched() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDtos.add(userRoleDto);
        userRoleDto.setRoleId("aaa");
        userRoleDto.setResourceType("common");
        userRoleDto.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new PermissionController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var requestMappingInfo =
                    RequestMappingInfo.paths("/common/initialization-default-permissions-data").build();
            HandlerMethod handlerMethod =
                    new HandlerMethod(new TestController(), TestController.class.getDeclaredMethod("normal"));
            handlerMethods.put(requestMappingInfo, handlerMethod);
            requestMappingInfo = RequestMappingInfo.paths("/common/other").build();
            handlerMethods.put(requestMappingInfo, handlerMethod);
            // Invoke
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(new LinkedList<Permission>());
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);
            assertThrowsExactly(IllegalStateException.class, () -> apiAuthorizationCheck.verifyPutRequest(joinPoint));
        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyPatchRequest_isAdmin() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDtos.add(userRoleDto);
        userRoleDto.setRoleId("SystemAdmin");
        userRoleDto.setResourceType("common");
        userRoleDto.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new PermissionController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var requestMappingInfo =
                    RequestMappingInfo.paths("/common/initialization-default-permissions-data").build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);
            requestMappingInfo = RequestMappingInfo.paths("/common/other").build();
            handlerMethods.put(requestMappingInfo, handlerMethod);
            // Invoke
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(new LinkedList<Permission>());
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);
            apiAuthorizationCheck.verifyPatchRequest(joinPoint);
            // Verify
            verify(methodInvocation, times(1)).proceed();
            verify(methodInvocation, times(1)).getThis();
        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyDeleteRequest_notAdmin_NoPermission_followDefault() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDtos.add(userRoleDto);
        userRoleDto.setRoleId("normal_role");
        userRoleDto.setResourceType("common");
        userRoleDto.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new PermissionController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/common/initialization-default-permissions-data";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);
            requestMappingInfo = RequestMappingInfo.paths("/common/other").build();
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(new LinkedList<Permission>());
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Request
            request.setRequestURI(url);

            Field field = PowerMockito.field(ApiAuthorizationCheck.class, "permissionDefaultControl");
            field.setAccessible(true);

            // Verify
            verify(methodInvocation, times(0)).proceed();

            // Invoke for default ALLOWED
            permissionDefaultControl = AccessControl.ALLOWED;
            field.set(apiAuthorizationCheck, permissionDefaultControl);
            apiAuthorizationCheck.verifyDeleteRequest(joinPoint);
            // Verify
            verify(methodInvocation, times(1)).proceed();

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyPostRequest_notAdmin_RoleNotMatched_followDefault() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDtos.add(userRoleDto);
        userRoleDto.setRoleId("normal_role");
        userRoleDto.setResourceType("not_matched_resource_type");
        userRoleDto.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new PermissionController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/common/initialization-default-permissions-data";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);
            requestMappingInfo = RequestMappingInfo.paths("/common/other").build();
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(new LinkedList<Permission>());
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            Field field = PowerMockito.field(ApiAuthorizationCheck.class, "permissionDefaultControl");
            field.setAccessible(true);

            // Verify
            verify(methodInvocation, times(0)).proceed();

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyPutRequest_notAdmin_permissionMatchedAllowed() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/another-normal-2";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType(userRole.getRoleId(),
                    userRole.getResourceType(), sort)).thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PUT);
            permission.setUrl(url);
            permission.setAction(ResourceAction.COMMON_ACTION_VIEW);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED);
            permission.setResourceType(userRole.getResourceType());
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            apiAuthorizationCheck.verifyPutRequest(joinPoint);
            // Verify
            verify(methodInvocation, times(1)).proceed();
            verify(methodInvocation, times(1)).getThis();

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyPostRequest_notAdmin_permissionMatchedDenied() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("common");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new PermissionController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/common/initialization-default-permissions-data";
            var requestMappingInfo = RequestMappingInfo.paths("/common/abc", url, "/abc").build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType(userRole.getRoleId(),
                    userRole.getResourceType(), sort)).thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(null);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.DENIED);
            permission.setResourceType(userRole.getResourceType());
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            assertThrowsExactly(AccessDeniedException.class, () -> apiAuthorizationCheck.verifyPostRequest(joinPoint));
        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyPostRequest_notAdmin_permissionMatchedWithAnyUrl() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/another-normal-2";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.POST);
            permission.setUrl(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setAction(ResourceAction.COMMON_ACTION_VIEW);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED);
            permission.setResourceType(userRole.getResourceType());
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            apiAuthorizationCheck.verifyPostRequest(joinPoint);
            // Verify
            verify(methodInvocation, times(1)).proceed();
            verify(methodInvocation, times(1)).getThis();

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_notAdmin_permissionMatchedWithAnyAction() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/another-normal-2";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.DELETE.name());

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.DELETE);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED);
            permission.setResourceType(userRole.getResourceType());
            permissions.add(permission);
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            apiAuthorizationCheck.verifyGenericRequest(joinPoint);
            // Verify
            verify(methodInvocation, times(1)).proceed();
            verify(methodInvocation, times(1)).getThis();

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_notAdmin_methodNotMatched() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new PermissionController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/common/initialization-default-permissions-data";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.POST.name());

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.DELETE);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED);
            permission.setResourceType(userRole.getResourceType());
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            assertThrowsExactly(AccessDeniedException.class,
                    () -> apiAuthorizationCheck.verifyGenericRequest(joinPoint));

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_notAdmin_urlNotMatched() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new PermissionController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/common/initialization-default-permissions-data";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI("not_matched_url");
            request.setMethod(RequestMethod.PUT.name());

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PUT);
            permission.setUrl("not_matched_url");
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED);
            permission.setResourceType(userRole.getResourceType());
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            assertThrowsExactly(AccessDeniedException.class,
                    () -> apiAuthorizationCheck.verifyGenericRequest(joinPoint));

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_notAdmin_ActionNotMatched() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new PermissionController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/common/initialization-default-permissions-data";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.DELETE.name());

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.DELETE);
            permission.setUrl(url);
            permission.setAction("In-correct-action");
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED);
            permission.setResourceType(userRole.getResourceType());
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            assertThrowsExactly(AccessDeniedException.class,
                    () -> apiAuthorizationCheck.verifyGenericRequest(joinPoint));

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_AllowedByResourceMatched() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/another-normal-2";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.PATCH.name());
            PowerMockito.when(methodInvocation.getArguments())
                    .thenReturn(new Object[]{"1", new MockHttpServletResponse()});

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PATCH);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED_SPECIFIC_RESOURCES);
            permission.setResourceType("test-common");
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Resource verifier
            ResourceOwnerVerifier resourceOwnerVerifier = PowerMockito.mock(ResourceOwnerVerifier.class);
            resourceOwnerVerifiers.add(resourceOwnerVerifier);
            PowerMockito.when(resourceOwnerVerifier.isSupportedResource("custom-resource-type")).thenReturn(true);
            PowerMockito.when(resourceOwnerVerifier.verifyOwner(jwtTokenDto, "custom-resource-type", "1", permission,
                            userRoleDtos.iterator().next()))
                    .thenReturn(true);

            apiAuthorizationCheck.verifyGenericRequest(joinPoint);
            // Verify
            verify(methodInvocation, times(1)).proceed();
            verify(methodInvocation, times(1)).getThis();

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_DeniedByResourceMatched() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/another-normal-2";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.PATCH.name());
            PowerMockito.when(methodInvocation.getArguments())
                    .thenReturn(new Object[]{"1", new MockHttpServletResponse()});

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PATCH);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.DENIED_SPECIFIC_RESOURCES);
            permission.setResourceType("test-common");
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Resource verifier
            ResourceOwnerVerifier resourceOwnerVerifier = PowerMockito.mock(ResourceOwnerVerifier.class);
            resourceOwnerVerifiers.add(resourceOwnerVerifier);
            PowerMockito.when(resourceOwnerVerifier.isSupportedResource("custom-resource-type")).thenReturn(true);
            PowerMockito.when(resourceOwnerVerifier.verifyOwner(jwtTokenDto, "custom-resource-type", "1", permission,
                            userRoleDtos.iterator().next()))
                    .thenReturn(true);

            // Invoke
            assertThrowsExactly(AccessDeniedException.class,
                    () -> apiAuthorizationCheck.verifyGenericRequest(joinPoint));

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_DeniedResourceNotMatched() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/another-normal-2";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.PATCH.name());
            PowerMockito.when(methodInvocation.getArguments())
                    .thenReturn(new Object[]{"1", new MockHttpServletResponse()});

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PATCH);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.DENIED_SPECIFIC_RESOURCES);
            permission.setResourceType("test-common");
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Resource verifier
            ResourceOwnerVerifier resourceOwnerVerifier = PowerMockito.mock(ResourceOwnerVerifier.class);
            resourceOwnerVerifiers.add(resourceOwnerVerifier);
            PowerMockito.when(resourceOwnerVerifier.isSupportedResource("custom-resource-type")).thenReturn(false);

            resourceOwnerVerifier = PowerMockito.mock(ResourceOwnerVerifier.class);
            resourceOwnerVerifiers.add(resourceOwnerVerifier);
            PowerMockito.when(resourceOwnerVerifier.isSupportedResource("custom-resource-type")).thenReturn(true);
            PowerMockito.when(resourceOwnerVerifier.verifyOwner(jwtTokenDto, "custom-resource-type", "1", permission,
                            userRoleDtos.iterator().next()))
                    .thenReturn(false);

            // Invoke
            apiAuthorizationCheck.verifyGenericRequest(joinPoint);
            // Verify
            verify(methodInvocation, times(1)).proceed();
            verify(methodInvocation, times(1)).getThis();

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_AllowedResourceNotMatched() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/another-normal-2";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.PATCH.name());
            PowerMockito.when(methodInvocation.getArguments())
                    .thenReturn(new Object[]{"1", new MockHttpServletResponse()});

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PATCH);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED_SPECIFIC_RESOURCES);
            permission.setResourceType("test-common");
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Resource verifier
            ResourceOwnerVerifier resourceOwnerVerifier = PowerMockito.mock(ResourceOwnerVerifier.class);
            resourceOwnerVerifiers.add(resourceOwnerVerifier);
            PowerMockito.when(resourceOwnerVerifier.isSupportedResource("custom-resource-type")).thenReturn(true);
            PowerMockito.when(resourceOwnerVerifier.verifyOwner(jwtTokenDto, "custom-resource-type", "1", permission,
                            userRoleDtos.iterator().next()))
                    .thenReturn(false);

            // Invoke
            assertThrowsExactly(AccessDeniedException.class,
                    () -> apiAuthorizationCheck.verifyGenericRequest(joinPoint));

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_DeniedByNoVerifier() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/another-normal-2";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.PATCH.name());
            PowerMockito.when(methodInvocation.getArguments())
                    .thenReturn(new Object[]{"1", new MockHttpServletResponse()});

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PATCH);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED_SPECIFIC_RESOURCES);
            permission.setResourceType("test-common");
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Invoke
            assertThrowsExactly(AccessDeniedException.class,
                    () -> apiAuthorizationCheck.verifyGenericRequest(joinPoint));

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_DeniedByNoResourceAnn() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal3",
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/another-normal-3";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.PUT.name());
            PowerMockito.when(methodInvocation.getArguments())
                    .thenReturn(new Object[]{"1", new MockHttpServletResponse()});

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PUT);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED_SPECIFIC_RESOURCES);
            permission.setResourceType("test-common");
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Resource verifier
            ResourceOwnerVerifier resourceOwnerVerifier = PowerMockito.mock(ResourceOwnerVerifier.class);
            resourceOwnerVerifiers.add(resourceOwnerVerifier);
            PowerMockito.when(resourceOwnerVerifier.isSupportedResource("custom-resource-type")).thenReturn(true);
            PowerMockito.when(resourceOwnerVerifier.verifyOwner(jwtTokenDto, "custom-resource-type", "1", permission,
                            userRoleDtos.iterator().next()))
                    .thenReturn(true);

            // Invoke
            assertThrowsExactly(AccessDeniedException.class,
                    () -> apiAuthorizationCheck.verifyGenericRequest(joinPoint));

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_DeniedByResourceNull() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("anotherNormal2", String.class,
                    HttpServletResponse.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/another-normal-2";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.PATCH.name());
            PowerMockito.when(methodInvocation.getArguments())
                    .thenReturn(new Object[]{null, new MockHttpServletResponse()});

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PATCH);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED_SPECIFIC_RESOURCES);
            permission.setResourceType("test-common");
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Resource verifier
            ResourceOwnerVerifier resourceOwnerVerifier = PowerMockito.mock(ResourceOwnerVerifier.class);
            resourceOwnerVerifiers.add(resourceOwnerVerifier);
            PowerMockito.when(resourceOwnerVerifier.isSupportedResource("custom-resource-type")).thenReturn(true);
            PowerMockito.when(resourceOwnerVerifier.verifyOwner(jwtTokenDto, "custom-resource-type", null, permission,
                            userRoleDtos.iterator().next()))
                    .thenReturn(false);

            // Invoke
            assertThrowsExactly(AccessDeniedException.class,
                    () -> apiAuthorizationCheck.verifyGenericRequest(joinPoint));

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_complexResourceId_nullResource1() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("complexResource", HttpServletResponse.class,
                    UserWrapterTest.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/request-with-complex-resource";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.PATCH.name());
            PowerMockito.when(methodInvocation.getArguments())
                    .thenReturn(new Object[]{new MockHttpServletResponse(), null});

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PATCH);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED_SPECIFIC_RESOURCES);
            permission.setResourceType("test-common");
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Resource verifier
            ResourceOwnerVerifier resourceOwnerVerifier = PowerMockito.mock(ResourceOwnerVerifier.class);
            resourceOwnerVerifiers.add(resourceOwnerVerifier);
            PowerMockito.when(resourceOwnerVerifier.isSupportedResource("custom-resource-type")).thenReturn(true);
            PowerMockito.when(resourceOwnerVerifier.verifyOwner(jwtTokenDto, "custom-resource-type", null, permission,
                            userRoleDtos.iterator().next()))
                    .thenReturn(false);

            // Invoke
            assertThrowsExactly(AccessDeniedException.class,
                    () -> apiAuthorizationCheck.verifyGenericRequest(joinPoint));

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_complexResourceId_nullResource2() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("complexResource", HttpServletResponse.class,
                    UserWrapterTest.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/request-with-complex-resource";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.PATCH.name());
            PowerMockito.when(methodInvocation.getArguments())
                    .thenReturn(new Object[]{new MockHttpServletResponse(), new UserWrapterTest(new UserTest())});

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PATCH);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED_SPECIFIC_RESOURCES);
            permission.setResourceType("test-common");
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Resource verifier
            ResourceOwnerVerifier resourceOwnerVerifier = PowerMockito.mock(ResourceOwnerVerifier.class);
            resourceOwnerVerifiers.add(resourceOwnerVerifier);
            PowerMockito.when(resourceOwnerVerifier.isSupportedResource("custom-resource-type")).thenReturn(true);
            PowerMockito.when(resourceOwnerVerifier.verifyOwner(jwtTokenDto, "custom-resource-type", null, permission,
                            userRoleDtos.iterator().next()))
                    .thenReturn(false);

            // Invoke
            assertThrowsExactly(AccessDeniedException.class,
                    () -> apiAuthorizationCheck.verifyGenericRequest(joinPoint));

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }

    @Test
    public void test_verifyGenericRequest_complexResourceId_AllowedByResource() throws Throwable {
        // Authen
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        List<UserRoleDto> userRoleDtos = new LinkedList<>();
        jwtTokenDto.setUserRoles(userRoleDtos);
        UserRoleDto userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("normal_role");
        userRole.setResourceType("test-common");
        userRole.setResourceId(null);
        userRole = new UserRoleDto();
        userRoleDtos.add(userRole);
        userRole.setRoleId("other_role");
        userRole.setResourceType("mismatched_type");
        userRole.setResourceId(null);
        try {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            // Controller target
            var controller = new AnotherTestController();
            // Method
            var method = AnotherTestController.class.getDeclaredMethod("complexResource", HttpServletResponse.class,
                    UserWrapterTest.class);
            // Handlers
            var handlerMethods = new HashMap<RequestMappingInfo, HandlerMethod>();
            PowerMockito.when(mapping.getHandlerMethods()).thenReturn(handlerMethods);
            var url = "/request-with-complex-resource";
            var requestMappingInfo = RequestMappingInfo.paths(url).build();
            HandlerMethod handlerMethod = new HandlerMethod(controller, method);
            handlerMethods.put(requestMappingInfo, handlerMethod);

            // Request
            request.setRequestURI(url);
            request.setMethod(RequestMethod.PATCH.name());
            var user = new UserTest();
            user.setId("usr1");
            PowerMockito.when(methodInvocation.getArguments())
                    .thenReturn(new Object[]{new MockHttpServletResponse(), new UserWrapterTest(user)});

            // Permissions
            List<Permission> permissions = new LinkedList<>();
            var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "action"), new Sort.Order(Sort.Direction.ASC, "url"));
            PowerMockito.when(permissionRepository.findAllByRoleIdAndResourceType("normal_role", "test-common", sort))
                    .thenReturn(permissions);
            var permission = new Permission();
            permission.setRequestMethod(RequestMethod.PATCH);
            permission.setUrl(url);
            permission.setAction(Permission.ANY_ROLE_APPLIED_VALUE);
            permission.setRoleId(userRole.getRoleId());
            permission.setControl(AccessControl.ALLOWED_SPECIFIC_RESOURCES);
            permission.setResourceType("test-common");
            permissions.add(permission);

            // Injects
            PowerMockito.when(methodInvocation.getThis()).thenReturn(controller);
            PowerMockito.when(methodInvocation.getMethod()).thenReturn(method);
            var response = GeneralApiResponse.createSuccessResponse(permissions);
            PowerMockito.when(methodInvocation.proceed()).thenReturn(response);

            // Resource verifier
            ResourceOwnerVerifier resourceOwnerVerifier = PowerMockito.mock(ResourceOwnerVerifier.class);
            resourceOwnerVerifiers.add(resourceOwnerVerifier);
            PowerMockito.when(resourceOwnerVerifier.isSupportedResource("custom-resource-type")).thenReturn(true);
            PowerMockito.when(
                            resourceOwnerVerifier.verifyOwner(jwtTokenDto, "custom-resource-type", "usr1", permission,
                                    userRoleDtos.iterator().next()))
                    .thenReturn(true);

            // Invoke
            // Invoke
            apiAuthorizationCheck.verifyGenericRequest(joinPoint);
            // Verify
            verify(methodInvocation, times(1)).proceed();
            verify(methodInvocation, times(1)).getThis();

        } finally {
            JwtContext.getInstance().removeJwt();
        }
    }
}
