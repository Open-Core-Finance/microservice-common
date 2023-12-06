package tech.corefinance.common.jpa.tenancy;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.corefinance.common.context.TenantContext;

import javax.annotation.Nonnull;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "tech.corefinance.schema-tenancy", name = "enabled", havingValue = "true")
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Autowired
    private TenancyProperties tenancyProperties;

    @Override
    @Nonnull
    public String resolveCurrentTenantIdentifier() {
        var tenantId = TenantContext.getInstance().getTenantId();
        if (!StringUtils.hasText(tenantId)) {
            return tenancyProperties.getDefaultSchema();
        }
        log.info("Tenant ID [{}]", tenantId);
        return getSchemaName(tenantId);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return tenancyProperties.isValidateExistingCurrentSessions();
    }

    private String getSchemaName(String tenantId) {
        var postfix = tenantId.replace("-", "_").replace(" ", "_");
        return tenancyProperties.getSchemaPrefix() + postfix;
    }
}
