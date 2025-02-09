package tech.corefinance.common.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tech.corefinance.common.config.JwtConfiguration;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.dto.UserRoleDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.enums.CommonConstants;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.model.AppVersion;
import tech.corefinance.common.repository.ResourceActionRepository;
import tech.corefinance.common.service.JwtService;
import tech.corefinance.common.service.JwtServiceImpl;
import tech.corefinance.common.service.JwtTokenParserImpl;
import tech.corefinance.common.test.support.app.TestCommonApplication;
import tech.corefinance.common.test.support.model.RoleTest;
import tech.corefinance.common.test.support.model.UserTest;
import tech.corefinance.common.test.support.pojo.JwtTokenDtoWithNonSerializable;
import tech.corefinance.common.test.support.pojo.JwtTokenDtoWithStaticFinal;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestCommonApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles({"common", "default", "unittest"})
@ComponentScan(basePackages = {"tech.corefinance"})
@Slf4j
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private JwtConfiguration jwtConfiguration;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ResourceActionRepository resourceActionRepository;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        PowerMockito.when(resourceActionRepository.saveAll(Mockito.any())).thenReturn(new LinkedList<>());
    }

    @Test
    public void test_retrieveTokenFromRequest_InvalidTokenLength() throws IOException {
        request.addHeader(HttpHeaders.AUTHORIZATION, "test");
        Map<String, JwtTokenDto> map = jwtService.retrieveTokenFromRequest(request, response);
        JwtTokenDto jwtTokenDto = null;
        Set<String> set = map.keySet();
        for (String key : set) {
            jwtTokenDto = map.get(key);
        }
        assertNull(jwtTokenDto);
    }

    @Test
    public void test_retrieveTokenFromRequest_NoData() throws IOException {
        Map<String, JwtTokenDto> map = jwtService.retrieveTokenFromRequest(request, response);
        JwtTokenDto jwtTokenDto = null;
        Set<String> set = map.keySet();
        for (String key : set) {
            jwtTokenDto = map.get(key);
        }
        assertNull(jwtTokenDto);
    }

    @Test
    public void test_retrieveTokenFromRequest_withExternalIpHeader() throws IOException {
        String deviceId = "Test-Device";
        UserTest admin = new UserTest("admin@email.com", "admin", "", "", "");
        admin.addRoleInSchool(new UserRoleDto("school", "01", "admin"));
        admin.setId(UUID.randomUUID().toString());
        JwtTokenDto loginInfo = new JwtTokenDto(UUID.randomUUID().toString(), admin.getId(), "ClientId",
                AppPlatform.ANDROID, new AppVersion(), deviceId, "127.0.1.1");
        String token = jwtService.buildLoginToken(loginInfo);
        request.addHeader(HttpHeaders.AUTHORIZATION, CommonConstants.BEARER_PREFIX + token);
        request.addHeader(CommonConstants.HEADER_KEY_EXTERNAL_IP_ADDRESS, "127.0.1.1");
        request.addHeader(CommonConstants.DEVICE_ID, deviceId);
        Map<String, JwtTokenDto> map = jwtService.retrieveTokenFromRequest(request, response);
        JwtTokenDto jwtTokenDto = null;
        Set<String> set = map.keySet();
        for (String key : set) {
            jwtTokenDto = map.get(key);
        }
        assertNotNull(jwtTokenDto);
    }

    @Test
    public void test_retrieveTokenFromRequest_withExternalIpHeader_IpNotMatched() throws JsonProcessingException {
        String deviceId = "Test-Device";
        UserTest admin = new UserTest("admin@email.com", "admin", "", "", "");
        admin.addRoleInSchool(new UserRoleDto("school", "01", "admin"));
        admin.setId(UUID.randomUUID().toString());
        JwtTokenDto loginInfo = new JwtTokenDto(UUID.randomUUID().toString(), admin.getId(), "ClientId",
                AppPlatform.ANDROID, new AppVersion(), deviceId, "127.0.1.1");
        String token = jwtService.buildLoginToken(loginInfo);
        request.addHeader(HttpHeaders.AUTHORIZATION, CommonConstants.BEARER_PREFIX + token);
        request.addHeader(CommonConstants.HEADER_KEY_EXTERNAL_IP_ADDRESS, "127.0.1.1");
        request.addHeader(CommonConstants.DEVICE_ID, "In-correct DeviceID");
        assertThrows(JWTVerificationException.class, () -> jwtService.retrieveTokenFromRequest(request, response));
    }

    @Test
    public void test_retrieveTokenFromRequest_deviceIdNotMatched() throws JsonProcessingException {
        String deviceId = "Test-Device";
        UserTest admin = new UserTest("admin@email.com", "admin", "", "", "");
        admin.addRoleInSchool(new UserRoleDto("school", "01", "admin"));
        admin.setId(UUID.randomUUID().toString());
        JwtTokenDto loginInfo = new JwtTokenDto(UUID.randomUUID().toString(), admin.getId(), "ClientId",
                AppPlatform.ANDROID, new AppVersion(), deviceId, "127.0.1.1");
        String token = jwtService.buildLoginToken(loginInfo);
        request.addHeader(HttpHeaders.AUTHORIZATION, CommonConstants.BEARER_PREFIX + token);
        request.addHeader(CommonConstants.DEVICE_ID, "anydevice");
        assertThrows(JWTVerificationException.class, () -> jwtService.retrieveTokenFromRequest(request, response));
    }

    @Test
    public void test_retrieveTokenFromRequest_NoPrivateKey() throws Exception {
        String deviceId = "Test-Device";
        UserTest admin = new UserTest("admin@email.com", "admin", "", "", "");
        admin.addRoleInSchool(new UserRoleDto("school", "01", "admin"));
        admin.setId(UUID.randomUUID().toString());
        JwtTokenDto loginInfo = new JwtTokenDto(UUID.randomUUID().toString(), admin.getId(), "ClientId",
                AppPlatform.ANDROID, new AppVersion(), deviceId, "127.0.1.1");
        String token = jwtService.buildLoginToken(loginInfo);
        request.addHeader(HttpHeaders.AUTHORIZATION, CommonConstants.BEARER_PREFIX + token);
        request.addHeader(CommonConstants.HEADER_KEY_EXTERNAL_IP_ADDRESS, "127.0.1.1");
        request.addHeader(CommonConstants.DEVICE_ID, deviceId);
        JwtService jwtService2 = new JwtServiceImpl("classpath:public_key.der", "", resourceLoader);
        PowerMockito.field(JwtServiceImpl.class, "objectMapper").set(jwtService2, objectMapper);
        PowerMockito.field(JwtServiceImpl.class, "jwtConfiguration").set(jwtService2, jwtConfiguration);
        var jwtTokenParser = new JwtTokenParserImpl(objectMapper);
        PowerMockito.field(JwtServiceImpl.class, "jwtTokenParser").set(jwtService2, jwtTokenParser);
        assertNotNull(jwtService2.retrieveTokenFromRequest(request, response));
        assertThrows(SignatureGenerationException.class, () -> jwtService2.sign(new HashMap<>()));
    }

    /**
     * This test need to make sure all kind of data work. That's why there are a lot assert in this test.
     */
    @Test
    public void test_sign() {
        String deviceId = "Test device";
        String ipaddress = "127.0.0.1";
        Map<String, Serializable> dataToSign = new HashMap<>();
        dataToSign.put(CommonConstants.ATTRIBUTE_NAME_DEVICE_ID, deviceId);
        dataToSign.put(CommonConstants.ATTRIBUTE_NAME_IP_ADDRESS, ipaddress);
        // Setting data
        Date date = new Date();
        ArrayList<String> permissions = new ArrayList<>(List.of("ADMIN_DASHBOARD", "ALL_ORDER_LIST"));
        HashSet<String> collection = new HashSet<>();
        collection.add("Collection Item 01");
        collection.add("Collection Item 02");
        HashMap<String, Serializable> subMap = new HashMap<>();
        subMap.put("subitem01", "01");
        subMap.put("subitem02", "02");
        dataToSign.put("nullfield", null);
        dataToSign.put("IntegerField", 3);
        dataToSign.put("BooleanField", true);
        dataToSign.put("DoubleField", 1.2);
        dataToSign.put("LongField", 12L);
        dataToSign.put("DateField", date);
        dataToSign.put("StrArrField", new String[]{deviceId, ipaddress});
        dataToSign.put("IntArrField", new int[]{3, 1, 2});
        dataToSign.put("IntegerArrField", new Integer[]{5, 4, 3});
        dataToSign.put("LongArrField", new long[]{3, 1, 2});
        dataToSign.put("AnotherLongArrField", new Long[]{5L, 4L, 3L});
        dataToSign.put("ListField", permissions);
        dataToSign.put("CollectionField", collection);
        dataToSign.put("MapField", subMap);
        dataToSign.put("NotSupportedObject", new Serializable() {
            @Override
            public int hashCode() {
                return -1;
            }
        });
        // Sign
        String token = jwtService.sign(dataToSign);
        DecodedJWT decodedJWT = jwtService.verify(token, deviceId, ipaddress);
        assertEquals(permissions, decodedJWT.getClaim("ListField").asList(String.class));
        assertEquals(3, decodedJWT.getClaim("IntegerField").asInt());
        assertEquals(true, decodedJWT.getClaim("BooleanField").asBoolean());
        assertEquals(1.2, decodedJWT.getClaim("DoubleField").asDouble());
        assertEquals(12L, decodedJWT.getClaim("LongField").asLong());
        assertArrayEquals(new String[]{deviceId, ipaddress}, decodedJWT.getClaim("StrArrField").asArray(String.class));
        assertArrayEquals(new Integer[]{3, 1, 2}, decodedJWT.getClaim("IntArrField").asArray(Integer.class));
        assertArrayEquals(new Integer[]{5, 4, 3}, decodedJWT.getClaim("IntegerArrField").asArray(Integer.class));
        assertArrayEquals(new Long[]{3L, 1L, 2L}, decodedJWT.getClaim("LongArrField").asArray(Long.class));
        assertArrayEquals(new Long[]{5L, 4L, 3L}, decodedJWT.getClaim("AnotherLongArrField").asArray(Long.class));
        assertArrayEquals(collection.toArray(), decodedJWT.getClaim("CollectionField").asList(String.class).toArray());
        assertEquals(subMap, decodedJWT.getClaim("MapField").asMap());
        assertEquals(date.getTime() / 1000, decodedJWT.getClaim("DateField").asDate().getTime() / 1000);
        assertTrue(decodedJWT.getClaim("NotSupportedObject").isMissing());
    }

    @Test
    public void test_buildRefreshToken() throws JsonProcessingException {
        String deviceId = "Test device";
        UserTest admin = new UserTest("admin@email.com", "admin", "", "", "");
        JwtTokenDto loginInfo = new JwtTokenDto(UUID.randomUUID().toString(), admin.getId(), "ClientId",
                AppPlatform.ANDROID, new AppVersion(), deviceId, "127.0.1.1");
        loginInfo.setUserEmail(admin.getEmail());
        loginInfo.setUsername(admin.getUsername());
        String token = jwtService.buildRefreshToken(loginInfo, jwtService.buildLoginToken(loginInfo));
        DecodedJWT decodedJWT = jwtService.verify(token, loginInfo.getDeviceId(), loginInfo.getLoginIpAddr());
        log.info("Decoded json: [{}]", decodedJWT.getClaims());
        assertEquals(loginInfo.getLoginId(), decodedJWT.getClaim("loginId").asString());
        assertEquals(loginInfo.getUserId(), decodedJWT.getClaim("userId").asString());
    }

    @Test
    public void test_buildRefreshToken_noLoginToken() {
        String deviceId = "Test device";
        UserTest admin = new UserTest("admin@email.com", "admin", "", "", "");
        JwtTokenDto loginInfo = new JwtTokenDto(UUID.randomUUID().toString(), admin.getId(), "ClientId",
                AppPlatform.ANDROID, new AppVersion(), deviceId, "127.0.1.1");
        loginInfo.setUserEmail(admin.getEmail());
        loginInfo.setUsername(admin.getUsername());
        assertThrows(ServiceProcessingException.class, () -> jwtService.buildRefreshToken(loginInfo, ""));
    }

    @Test
    public void test_buildAuthenToken() throws Exception {
        String deviceId = "Test-Device";
        int loginExpiry = 3600;
        // Sign token
        RoleTest r = new RoleTest();
        r.setName("admin");
        r.setId(UUID.randomUUID().toString());
        UserTest admin = new UserTest("admin@email.com", "admin", "", "", "");
        admin.addRoleInSchool(new UserRoleDto("school", "01", "admin"));
        admin.setId(UUID.randomUUID().toString());
        JwtTokenDto loginInfo = new JwtTokenDto(UUID.randomUUID().toString(), admin.getId(), "ClientId",
                AppPlatform.ANDROID, new AppVersion(), deviceId, "127.0.1.1");
        loginInfo.setUserRoles(admin.getRolesInSchools());
        loginInfo.setUserEmail(admin.getEmail());
        long expiredIn = System.currentTimeMillis() + loginExpiry * 1000;
        String token = jwtService.buildLoginToken(loginInfo);
        // Verify token
        DecodedJWT decodedJWT = jwtService.verify(token, deviceId, loginInfo.getLoginIpAddr());
        String json = new String(Base64.getDecoder().decode(decodedJWT.getPayload().getBytes()), StandardCharsets.UTF_8);
        log.info("Decoded json: [{}]", json);
        JwtTokenDto jwtTokenDto = objectMapper.readValue(json, JwtTokenDto.class);
        log.info("Deserialized JwtTokenDto: [{}]", jwtTokenDto);
        assertNotNull(jwtTokenDto);
        assertEquals(admin.getEmail(), jwtTokenDto.getUserEmail());
        assertTrue(jwtTokenDto.getExpiredIn() - expiredIn < 1000);
        assertEquals(loginInfo.getLoginIpAddr(), jwtTokenDto.getLoginIpAddr());
    }

    @Test
    void test_buildLoginDataMap_objectWithStatic() throws JsonProcessingException {
        var jwtData = jwtService.buildLoginDataMap(new JwtTokenDtoWithStaticFinal());
        assertNull(jwtData.get("ABC"));
        assertNotNull(jwtData.get("loginId"));
    }

    @Test
    void test_buildLoginDataMap_objectWithNonSerialize() throws JsonProcessingException {
        var jwtData = jwtService.buildLoginDataMap(new JwtTokenDtoWithNonSerializable());
        assertNotNull(jwtData.get("loginId"));
    }

    @Test
    void test_buildLoginVerifyKey_objectWithNonSerialize() throws JsonProcessingException {
        var jwtData = jwtService.buildLoginDataMap(new JwtTokenDtoWithNonSerializable());
        assertNotNull(jwtData.get("loginId"));
    }
}
