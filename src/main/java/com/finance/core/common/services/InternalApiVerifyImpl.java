package com.finance.core.common.services;

import com.finance.core.common.context.JwtContext;
import com.finance.core.common.enums.CommonConstants;
import com.finance.core.common.model.InternalServiceConfig;
import com.finance.core.common.annotation.InternalApi;
import com.finance.core.common.repository.InternalServiceConfigRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Optional;

@Service
public class InternalApiVerifyImpl implements InternalApiVerify {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
            logger.debug("Received call from service [{}]", config);
        }
        return true;
    }
}
