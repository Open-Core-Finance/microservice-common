package tech.corefinance.product.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.corefinance.common.context.TenantContext;
import tech.corefinance.product.controller.CurrencyController;
import tech.corefinance.product.controller.OrganizationController;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class OrganizationIgnoreTenantFilter implements Filter, Ordered {

    private static List<Class<?>> controllerToIgnoreTenant = List.of(OrganizationController.class, CurrencyController.class);
    private List<String> listApplyUrls = new LinkedList<>();
    
    @PostConstruct
    public void postConstruct() {
        controllerToIgnoreTenant.forEach( c -> {
            var values = c.getAnnotation(RequestMapping.class).value();
            listApplyUrls.addAll(List.of(values));
        });
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var requestUri = ((HttpServletRequest) request).getRequestURI();
        var tenantContext = TenantContext.getInstance();
        var currentTenant = tenantContext.getTenantId();
        log.debug("Current tenant [{}]", currentTenant);
        var isCallingOrganization = shouldIgnore(requestUri);
        try {
            if (isCallingOrganization) {
                log.debug("Clear tenant info for Organization service");
                TenantContext.getInstance().clearTenantId();
            }
            chain.doFilter(request, response);
        } finally {
            if (isCallingOrganization) {
                log.debug("Restore tenant [{}]", currentTenant);
                tenantContext.setTenantId(currentTenant);
            }
        }
    }

    private boolean shouldIgnore(String requestUri) {
        for (var url : listApplyUrls) {
            if (requestUri.startsWith(url)) {
                return true;
            }
        }
        return false;
    }
}
