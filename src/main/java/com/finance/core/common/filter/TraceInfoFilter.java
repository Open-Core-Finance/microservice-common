package com.finance.core.common.filter;

import com.finance.core.common.enums.CommonConstants;
import com.finance.core.common.context.TraceIdContext;
import jakarta.servlet.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@ConditionalOnProperty(prefix = "com.finance.core.common.enabled", name = "trace-id-filter", havingValue = "true",
        matchIfMissing = true)
public class TraceInfoFilter implements Filter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${application.trace-info-id-order:0}")
    private int order;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        logger.debug("Initializing trace info filter...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        TraceIdContext traceIdContext = TraceIdContext.getInstance();
        logger.debug("Checking trace id header [{}] in the request...", CommonConstants.HEADER_KEY_TRACE_ID);
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            String traceId = httpServletRequest.getHeader(CommonConstants.HEADER_KEY_TRACE_ID);
            logger.debug("Trace id value [{}]", traceId);
            if (StringUtils.isBlank(traceId)) {
                traceId = UUID.randomUUID().toString();
            }
            traceIdContext.setTraceId(traceId);
            httpServletResponse.setHeader(CommonConstants.HEADER_KEY_TRACE_ID, URLEncoder.encode(traceId,
                    StandardCharsets.US_ASCII));
            logger.debug("Set trace id [{}] to context and response.", traceId);
            logger.debug("Continue filter chain");
            chain.doFilter(request, response);
        } finally {
            logger.debug("Clear trace id in the context!");
            traceIdContext.clearTraceId();
        }
    }

    @Override
    public void destroy() {
        logger.debug("Destroy trace info filter!");
    }

}
