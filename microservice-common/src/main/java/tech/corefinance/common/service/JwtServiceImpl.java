package tech.corefinance.common.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import tech.corefinance.common.config.JwtConfiguration;
import tech.corefinance.common.context.TenantContext;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.enums.CommonConstants;
import tech.corefinance.common.ex.ServiceProcessingException;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.Map.Entry;

@Service
@ConditionalOnProperty(prefix = "tech.corefinance.security.jwt.enabled", name = "common", matchIfMissing = true,
        havingValue = "true")
@Slf4j
public class JwtServiceImpl implements JwtService {

    private static final String KEY_ALGORITHM = "RSA";

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private Algorithm algorithmRS;
    private JWTVerifier verifier;

    @Autowired
    private JwtConfiguration jwtConfiguration;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired(required = false)
    private List<JwtVerifyAddOn> jwtVerifyAddOns;
    @Autowired
    private JwtTokenParser jwtTokenParser;

    public JwtServiceImpl(@Value("${tech.corefinance.security.public-key}") String publicKey,
                          @Value("${tech.corefinance.security.private-key:}") String privateKey,
                          @Autowired ResourceLoader resourceLoader)
            throws GeneralSecurityException, IOException {
        log.debug("Creating JwtServiceImpl with key algorithm [{}]", KEY_ALGORITHM);
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
        // Public key
        log.debug("Loading public key [{}]", publicKey);
        Resource resource = resourceLoader.getResource(publicKey);
        byte[] decodedPublic = resource.getInputStream().readAllBytes();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedPublic);
        this.publicKey = (RSAPublicKey) kf.generatePublic(spec);
        // Private key
        if (!StringUtils.isBlank(privateKey)) {
            log.debug("Loading private key [{}]", privateKey);
            resource = resourceLoader.getResource(privateKey);
            byte[] decodedPrivate = resource.getInputStream().readAllBytes();
            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(decodedPrivate);
            this.privateKey = (RSAPrivateKey) kf.generatePrivate(privateSpec);
        } else {
            log.debug("Skipped private key!");
        }
        // Algorithm
        algorithmRS = Algorithm.RSA256(this.publicKey, this.privateKey);
        // Reusable verifier instance
        log.debug("Creating JWT verifier...");
        verifier = JWT.require(this.algorithmRS).withIssuer("auth0").build();
        log.debug("Creating JwtServiceImpl done!");
    }

    public JwtConfiguration getJwtConfiguration() {
        return jwtConfiguration;
    }

    @Override
    public String sign(Map<String, Serializable> data) {
        Builder builder = JWT.create().withIssuer("auth0");
        for (Entry<String, Serializable> entry : data.entrySet()) {
            addClaim(builder, entry.getKey(), entry.getValue());
        }
        builder.withIssuedAt(new Date());
        builder.withExpiresAt(new Date(System.currentTimeMillis() + jwtConfiguration.getExpiration() * 1000));
        return builder.sign(algorithmRS);
    }

    @SuppressWarnings("unchecked")
    private void addClaim(Builder builder, String key, Serializable value) {
        if (value == null) {
            builder.withClaim(key, (String) null);
            return;
        }
        Class<?> valueClzz = value.getClass();
        if (String.class.isAssignableFrom(valueClzz) || UUID.class.isAssignableFrom(valueClzz)) {
            builder.withClaim(key, value.toString());
        } else if (Integer.class.isAssignableFrom(valueClzz)) {
            builder.withClaim(key, (Integer) value);
        } else if (Boolean.class.isAssignableFrom(valueClzz)) {
            builder.withClaim(key, (Boolean) value);
        } else if (Double.class.isAssignableFrom(valueClzz)) {
            builder.withClaim(key, (Double) value);
        } else if (Long.class.isAssignableFrom(valueClzz)) {
            builder.withClaim(key, (Long) value);
        } else if (Date.class.isAssignableFrom(valueClzz)) {
            builder.withClaim(key, (Date) value);
        } else if (String[].class.isAssignableFrom(valueClzz)) {
            builder.withArrayClaim(key, (String[]) value);
        } else if (Integer[].class.isAssignableFrom(valueClzz)) {
            builder.withArrayClaim(key, (Integer[]) value);
        } else if (int[].class.isAssignableFrom(valueClzz)) {
            int[] val = (int[]) value;
            Integer[] arr = new Integer[val.length];
            for (int i = 0; i < val.length; i++) {
                arr[i] = val[i];
            }
            builder.withArrayClaim(key, arr);
        } else if (Long[].class.isAssignableFrom(valueClzz)) {
            builder.withArrayClaim(key, (Long[]) value);
        } else if (long[].class.isAssignableFrom(valueClzz)) {
            long[] val = (long[]) value;
            Long[] arr = new Long[val.length];
            for (int i = 0; i < val.length; i++) {
                arr[i] = val[i];
            }
            builder.withArrayClaim(key, arr);
        } else if (List.class.isAssignableFrom(valueClzz)) {
            log.debug("List value to sign {}", value);
            builder.withClaim(key, (List<?>) value);
        } else if (Collection.class.isAssignableFrom(valueClzz)) {
            log.debug("Collection value to sign {}", value);
            builder.withClaim(key, new ArrayList<Object>(((Collection<?>) value)));
        } else if (Map.class.isAssignableFrom(valueClzz)) {
            log.debug("Map value to sign {}", value);
            builder.withClaim(key, (Map<String, ?>) value);
        } else if (Enum.class.isAssignableFrom(valueClzz)) {
            builder.withClaim(key, ((Enum<?>) value).name());
        }
    }

    @Override
    public DecodedJWT verify(String token, String deviceId, String ipaddress) {
        log.debug("Verifying token [{}] with device [{}] and IP [{}]", token, deviceId, ipaddress);
        DecodedJWT decodedJWT = verifier.verify(token);
        log.debug("Decoded JWT [{}]", decodedJWT);
        if (deviceId == null) {
            if (decodedJWT.getClaim(CommonConstants.ATTRIBUTE_NAME_DEVICE_ID).asString() != null) {
                throw new JWTVerificationException("Device ID missing in request but existed in token!!");
            }
        } else {
            if (!deviceId.equalsIgnoreCase(decodedJWT.getClaim(CommonConstants.ATTRIBUTE_NAME_DEVICE_ID).asString())) {
                throw new JWTVerificationException("Device ID miss matched in token and request!!");
            }
        }
        if (!ipaddress.equalsIgnoreCase(decodedJWT.getClaim(CommonConstants.ATTRIBUTE_NAME_IP_ADDRESS).asString())) {
            throw new JWTVerificationException("IP Address [" + ipaddress + "] miss matched in token and request ["
                    + decodedJWT.getClaim(CommonConstants.ATTRIBUTE_NAME_IP_ADDRESS) + "]!!");
        }
        var requestTenant = TenantContext.getInstance().getTenantId();
        var tokenTenant = decodedJWT.getClaim("tenantId").asString();
        if (StringUtils.isNotBlank(requestTenant) && StringUtils.isNotBlank(tokenTenant)) {
            if (!tokenTenant.equalsIgnoreCase(requestTenant)) {
                throw new JWTVerificationException("Cross tenant requested!!");
            }
        }
        return decodedJWT;
    }

    @Override
    public String extractIpAddress(HttpServletRequest httpServletRequest) throws UnknownHostException {
        var listHeaders = new String[] {
                CommonConstants.HEADER_KEY_EXTERNAL_IP_ADDRESS, CommonConstants.HEADER_KEY_REAL_IP_ADDRESS,
                CommonConstants.HEADER_KEY_FORWARDED_FOR
        };
        for (var headerKey : listHeaders) {
            var ipAddress = httpServletRequest.getHeader(headerKey);
            if (!StringUtils.isBlank(ipAddress)) {
                log.debug("Found IP address in header [{}] is [{}]", headerKey, ipAddress);
                return ipAddress;
            }
        }
        var remoteIp = httpServletRequest.getRemoteAddr();
        log.debug("Retreived IP Address from request [{}]", remoteIp);
        return remoteIp;
    }

    @Override
    public Map<String, JwtTokenDto> retrieveTokenFromRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        // Get token and device ID in parameters
        log.debug("Retrieving data from request");
        String deviceId = httpServletRequest.getHeader(CommonConstants.DEVICE_ID);
        String ipAddress = extractIpAddress(httpServletRequest);
        log.debug("Device ID [{}] with ip address [{}]", deviceId, ipAddress);
        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        log.debug("authorization in header [{}]", authorizationHeader);
        Map<String, JwtTokenDto> map = new HashMap<>();
        JwtTokenDto jwtTokenDto;
        if (authorizationHeader != null) {
            if (authorizationHeader.length() <= CommonConstants.BEARER_PREFIX.length()) {
                log.error("Invalid bearer token found!!! [{}]", authorizationHeader);
            } else {
                String token = authorizationHeader.substring(CommonConstants.BEARER_PREFIX.length());
                DecodedJWT decodedJWT = verify(token, deviceId, ipAddress);
                String json = new String(Base64.getDecoder().decode(decodedJWT.getPayload().getBytes()), StandardCharsets.UTF_8);
                jwtTokenDto = jwtTokenParser.parse(json);
                jwtTokenDto.setOriginalToken(token);
                log.debug("Decoded token [{}]", jwtTokenDto);
                jwtTokenDto = additionalJwtVerifyStep(jwtTokenDto, token, deviceId, ipAddress);
                log.debug("Completed add on verify!");
                if (jwtTokenDto != null) {
                    map.put(authorizationHeader, jwtTokenDto);
                } else {
                    log.error("Token fail the validation!");
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, Serializable> buildLoginDataMap(JwtTokenDto jwtTokenDto) throws JsonProcessingException {
        log.debug("Building login map data...");
        Map<String, Serializable> jwtData = new HashMap<>();
        Class<?> clzz = jwtTokenDto.getClass();
        log.debug("Object class {}", clzz);
        putFieldData(jwtTokenDto, clzz, jwtData);
        // Additional data
        log.debug("Put addition data to login token");
        jwtData.putAll(jwtTokenDto.getAdditionalInfo());
        log.info("Login token before add custom attributes [{}]", jwtData);
        jwtData.put("expiredIn", System.currentTimeMillis() + jwtConfiguration.getExpiration() * 1000);
        jwtData.put(CommonConstants.ATTRIBUTE_NAME_APP_VERSION, objectMapper.writeValueAsString(jwtTokenDto.getAppVersion()));
        log.debug("Original role in school {}", jwtTokenDto.getUserRoles());
        LinkedList<String> userRoles = new LinkedList<>(jwtTokenDto.getUserRoles().stream().map(r -> {
            try {
                return objectMapper.writeValueAsString(r);
            } catch (JsonProcessingException e) {
                throw new ServiceProcessingException("error_sign_login_token", e);
            }
        }).toList());
        jwtData.put("userRoles", userRoles);
        jwtData.remove("additionalInfo");
        log.info("Login token before sign {}", jwtData);
        return jwtData;
    }

    private void putFieldData(JwtTokenDto jwtTokenDto, Class<?> clzz, Map<String, Serializable> jwtData) {
        Field[] fields = clzz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            log.debug("Checking field [{}] is static [{}]", fieldName,
                    Modifier.isStatic(field.getModifiers()));
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    log.debug("Getting field data....");
                    Object fieldData = PropertyUtils.getNestedProperty(jwtTokenDto, fieldName);
                    log.debug("Field data [{}]", fieldData);
                    if (fieldData != null && Serializable.class.isAssignableFrom(fieldData.getClass())) {
                        log.debug("Put field data to sign map");
                        jwtData.put(fieldName, (Serializable) fieldData);
                    } else {
                        log.debug("Field data is not Serializable.");
                    }
                } catch (ReflectiveOperationException e) {
                    throw new ServiceProcessingException("errors.server.invalid_jwt_field", e);
                }
            }
        }
        // Check parent class
        clzz = clzz.getSuperclass();
        if (JwtTokenDto.class.isAssignableFrom(clzz)) {
            log.debug("Re-check parent fields [{}]", clzz);
            putFieldData(jwtTokenDto, clzz, jwtData);
        }
    }

    public Map<String, Serializable> buildRefreshTokenDataMap(JwtTokenDto jwtTokenDto, String token)
            throws JsonProcessingException {
        Map<String, Serializable> refreshJwtData = new HashMap<>();
        refreshJwtData.put("loginId", jwtTokenDto.getLoginId());
        refreshJwtData.put("userId", jwtTokenDto.getUserId());
        if (StringUtils.isBlank(token)) {
            throw new ServiceProcessingException("Login handling error!!!");
        }
        // refreshJwtData.put("loginToken", token);
        refreshJwtData.put(CommonConstants.ATTRIBUTE_NAME_DEVICE_ID, jwtTokenDto.getDeviceId());
        refreshJwtData.put(CommonConstants.ATTRIBUTE_NAME_IP_ADDRESS, jwtTokenDto.getLoginIpAddr());
        refreshJwtData.put(CommonConstants.ATTRIBUTE_NAME_APP_PLATFORM, jwtTokenDto.getAppPlatform().name());
        refreshJwtData.put(CommonConstants.ATTRIBUTE_NAME_APP_VERSION, objectMapper.writeValueAsString(jwtTokenDto.getAppVersion()));
        log.debug("Put addition data to refresh token");
        refreshJwtData.putAll(jwtTokenDto.getAdditionalInfo());
        return refreshJwtData;
    }

    protected JwtTokenDto additionalJwtVerifyStep(JwtTokenDto jwtTokenDto, String token, String deviceId,
                                                  String ipaddress) {
        if (jwtVerifyAddOns != null) {
            log.debug("Configured JWT verify addon {}", jwtVerifyAddOns);
            for (JwtVerifyAddOn addOn : jwtVerifyAddOns) {
                jwtTokenDto = addOn.additionalJwtVerify(jwtTokenDto, token, deviceId, ipaddress);
                if (jwtTokenDto == null) {
                    return null;
                }
            }
        }
        return jwtTokenDto;
    }

    @Override
    public BasicUserDto retrieveUserAsAttribute(JwtTokenDto jwtTokenDto) {
        if(jwtTokenDto == null){
            return null;
        }
        BasicUserDto user = new BasicUserDto();
        user.setUserId(jwtTokenDto.getUserId());
        user.setEmail(jwtTokenDto.getUserEmail());
        user.setDisplayName(jwtTokenDto.getUserDisplayName());
        user.setUsername(jwtTokenDto.getUsername());
        return user;
    }
}
