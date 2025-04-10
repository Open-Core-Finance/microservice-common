package tech.corefinance.common.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tech.corefinance.common.annotation.InternalApi;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.entity_author.InternalServiceConfig;
import tech.corefinance.common.enums.CommonConstants;
import tech.corefinance.common.repository.InternalServiceConfigRepository;

import java.lang.reflect.Method;
import java.util.Optional;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public class InternalApiVerifyImpl implements InternalApiVerify {

    @Autowired
    private InternalServiceConfigRepository internalServiceConfigRepository;

    @Override
    public boolean internalPermissionCheck(Class<?> controllerClass, Method method, HttpServletRequest request) {
        var internalAnn = method.getAnnotation(InternalApi.class);
        if (internalAnn == null) {
            internalAnn = controllerClass.getAnnotation(InternalApi.class);
        }
        if (internalAnn != null) {
            if (internalAnn.needAuthenToken() && JwtContext.getInstance().getJwt() == null) {
                throw new AccessDeniedException("no_authen_token_found");
            }
            var internalApi = request.getHeader(CommonConstants.HEADER_KEY_INTERNAL_API_KEY);
            if (!StringUtils.hasText(internalApi)) {
                throw new AccessDeniedException("no_internal_key_found");
            }
            Optional<InternalServiceConfig> internalServiceConfigOptional = internalServiceConfigRepository.findFirstByApiKeyAndActivatedOrderByLastModifiedDateDesc(internalApi, true);
            var config = internalServiceConfigOptional.orElseThrow(() -> new AccessDeniedException("invalid_internal_api_key"));
            log.debug("Received call from service [{}]", config);
        }
        return true;
    }
}
