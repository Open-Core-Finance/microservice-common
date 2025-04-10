package tech.corefinance.common.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.corefinance.common.config.CommonFilterConfiguration;
import tech.corefinance.common.context.TenantContext;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "tech.corefinance.common.enabled", name = "tenant", havingValue = "true")
public class TenantIgnoreFilter implements Filter, Ordered {

    private final CommonFilterConfiguration filterConfiguration;
    private final List<String> listApplyUrls;

    public TenantIgnoreFilter(CommonFilterConfiguration filterConfiguration) {
        this.filterConfiguration = filterConfiguration;
        this.listApplyUrls = new LinkedList<>();
    }

    @PostConstruct
    public void postConstruct() {
        var ignoredControllerList = filterConfiguration.getIgnoreTenantControllers();
        if (ignoredControllerList != null) {
            var ignoredClasses = filterConfiguration.getIgnoreTenantControllers().stream().map(this::getClassForName).toList();
            log.debug("List ignored tenancy controllers {}", ignoredClasses);
            ignoredClasses.forEach(c -> {
                var values = c.getAnnotation(RequestMapping.class).value();
                listApplyUrls.addAll(List.of(values));
            });
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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

    private Class<?> getClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + className, e);
        }
    }
}
