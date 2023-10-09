package tech.corefinance.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.corefinance.common.context.TraceIdContext;
import tech.corefinance.common.enums.CommonConstants;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@ConditionalOnProperty(prefix = "tech.corefinance.common.enabled", name = "tenant", havingValue = "true")
@Slf4j
public class TenantIdFilter implements Filter, Ordered {

    @Value("${tech.corefinance.common.filter-ordered.tenant-id-filter-order:0}")
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
        log.debug("Checking tenant id header [{}] in the request...", CommonConstants.HEADER_KEY_TENANT_ID);
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            String tenantId = httpServletRequest.getHeader(CommonConstants.HEADER_KEY_TENANT_ID);
            log.debug("Tenant id value [{}]", tenantId);
            if (StringUtils.hasText(tenantId)) {
                traceIdContext.setTraceId(tenantId);
                httpServletResponse.setHeader(CommonConstants.HEADER_KEY_TRACE_ID, URLEncoder.encode(tenantId,
                        StandardCharsets.US_ASCII));
                log.debug("Set tenant id [{}] to context and response.", tenantId);
                log.debug("Continue filter chain");
            } else {
                log.debug("No Tenant ID in request!");
            }
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
