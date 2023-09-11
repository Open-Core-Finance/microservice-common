package tech.corefinance.common.services;

import tech.corefinance.common.config.JwtConfiguration;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.enums.CommonConstants;
import tech.corefinance.common.service.JwtService;
import tech.corefinance.common.service.JwtServiceImpl;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public class JwtServiceTestUsingMock {
    private static final String MOCK_PUBLIC_KEY = "MOCK_PUBLIC_KEY";
    private static final String MOCK_PRIVATE_KEY = "MOCK_PRIVATE_KEY";

    private JwtService jwtService;
    private ResourceLoader resourceLoader;
    private Resource publicKeyResource;
    private Resource privateKeyResource;

    @BeforeEach
    void setUp() throws GeneralSecurityException, IOException {
        // Public key
        InputStream ins = getClass().getClassLoader().getResourceAsStream("public_key.der");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(ins, baos);
        publicKeyResource = new ByteArrayResource(baos.toByteArray());
        // Private key
        ins = getClass().getClassLoader().getResourceAsStream("private_key.der");
        baos = new ByteArrayOutputStream();
        IOUtils.copy(ins, baos);
        privateKeyResource = new ByteArrayResource(baos.toByteArray());
        // Loader
        resourceLoader = PowerMockito.mock(ResourceLoader.class);
        PowerMockito.when(resourceLoader.getResource(MOCK_PUBLIC_KEY)).thenReturn(publicKeyResource);
        PowerMockito.when(resourceLoader.getResource(MOCK_PRIVATE_KEY)).thenReturn(privateKeyResource);
        // Create service
        jwtService = new JwtServiceImpl(MOCK_PUBLIC_KEY, MOCK_PRIVATE_KEY, resourceLoader);
    }

    @Test
    void test_buildLoginVerifyKey_SingleLoginMode() throws IllegalAccessException {
        JwtConfiguration jwtConfiguration = new JwtConfiguration();
        jwtConfiguration.setLoginMode(CommonConstants.JWT_VERIFY_MODE_SINGLE_LOGIN);
        PowerMockito.field(JwtServiceImpl.class, "jwtConfiguration").set(jwtService, jwtConfiguration);
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        jwtTokenDto.setUserId("userId");
        jwtTokenDto.setDeviceId("TestDevice");
        String key = jwtService.buildLoginVerifyKey(jwtTokenDto);
        Assertions.assertEquals(jwtTokenDto.getUserId(), key);
    }

    @Test
    void test_buildLoginVerifyKey_MultipleLoginMode() throws IllegalAccessException {
        JwtConfiguration jwtConfiguration = new JwtConfiguration();
        jwtConfiguration.setLoginMode(CommonConstants.JWT_VERIFY_MODE_MULTIPLE_LOGIN);
        PowerMockito.field(JwtServiceImpl.class, "jwtConfiguration").set(jwtService, jwtConfiguration);
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        jwtTokenDto.setUserId("userId");
        jwtTokenDto.setDeviceId("TestDevice");
        String key = jwtService.buildLoginVerifyKey(jwtTokenDto);
        Assertions.assertEquals(jwtTokenDto.getLoginId().toString(), key);
    }

    @Test
    void test_buildLoginVerifyKey_LoginPerAppMode() throws IllegalAccessException {
        JwtConfiguration jwtConfiguration = new JwtConfiguration();
        jwtConfiguration.setLoginMode(CommonConstants.JWT_VERIFY_MODE_SINGLE_LOGIN_PER_APP);
        PowerMockito.field(JwtServiceImpl.class, "jwtConfiguration").set(jwtService, jwtConfiguration);
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        jwtTokenDto.setUserId("userId");
        jwtTokenDto.setDeviceId("TestDevice");
        jwtTokenDto.setClientAppId("ClientAppId");
        String key = jwtService.buildLoginVerifyKey(jwtTokenDto);
        Assertions.assertEquals(jwtTokenDto.getUserId() + "||APP||" + jwtTokenDto.getClientAppId(), key);
    }

    @Test
    void test_buildLoginVerifyKey_LoginPerDeviceMode() throws IllegalAccessException {
        JwtConfiguration jwtConfiguration = new JwtConfiguration();
        jwtConfiguration.setLoginMode(CommonConstants.JWT_VERIFY_MODE_SINGLE_LOGIN_PER_DEVICE);
        PowerMockito.field(JwtServiceImpl.class, "jwtConfiguration").set(jwtService, jwtConfiguration);
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        jwtTokenDto.setUserId("userId");
        jwtTokenDto.setDeviceId("TestDevice");
        String key = jwtService.buildLoginVerifyKey(jwtTokenDto);
        Assertions.assertEquals(jwtTokenDto.getUserId() + "||DEVICE||" + jwtTokenDto.getDeviceId(), key);
    }
}
