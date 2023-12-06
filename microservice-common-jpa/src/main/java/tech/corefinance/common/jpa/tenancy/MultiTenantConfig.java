package tech.corefinance.common.jpa.tenancy;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Add custom connection and identifier providers.
 */
@Configuration
@ConditionalOnProperty(prefix = "tech.corefinance.schema-tenancy", name = "enabled", havingValue = "true")
public class MultiTenantConfig {

    /**
     * Create HibernatePropertiesCustomizer.
     * @param tenantConnectionProvider TenantConnectionProvider.
     * @param tenantIdentifierResolver TenantIdentifierResolver.
     * @return HibernatePropertiesCustomizer.
     */
    @Bean
    public HibernatePropertiesCustomizer multiTenantConfiguration(
            TenantConnectionProvider tenantConnectionProvider,
            TenantIdentifierResolver tenantIdentifierResolver
    ) {
        return hibernateProperties -> {
            hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, tenantConnectionProvider);
            hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        };
    }
}
