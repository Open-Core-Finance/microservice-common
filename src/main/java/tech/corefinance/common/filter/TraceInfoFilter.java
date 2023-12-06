package tech.corefinance.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import tech.corefinance.common.context.TraceIdContext;
import tech.corefinance.common.enums.CommonConstants;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@ConditionalOnProperty(prefix = "tech.corefinance.common.enabled", name = "trace-id-filter", havingValue = "true",
        matchIfMissing = true)
@Slf4j
public class TraceInfoFilter implements Filter, Ordered {

    @Value("${tech.corefinance.common.filter-ordered.trace-info-id-order:1}")
    private int order;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("Initializing trace info filter...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        TraceIdContext traceIdContext = TraceIdContext.getInstance();
        log.debug("Checking trace id header [{}] in the request...", CommonConstants.HEADER_KEY_TRACE_ID);
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            String traceId = httpServletRequest.getHeader(CommonConstants.HEADER_KEY_TRACE_ID);
            log.debug("Trace id value [{}]", traceId);
            if (StringUtils.isBlank(traceId)) {
                traceId = UUID.randomUUID().toString();
            }
            traceIdContext.setTraceId(traceId);
            httpServletResponse.setHeader(CommonConstants.HEADER_KEY_TRACE_ID, URLEncoder.encode(traceId,
                    StandardCharsets.US_ASCII));
            log.debug("Set trace id [{}] to context and response.", traceId);
            log.debug("Continue filter chain");
            chain.doFilter(request, response);
        } finally {
            log.debug("Clear trace id in the context!");
            traceIdContext.clearTraceId();
        }
    }

    @Override
    public void destroy() {
        log.debug("Destroy trace info filter!");
    }

}
