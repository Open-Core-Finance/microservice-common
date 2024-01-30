package tech.corefinance.common.jpa.dialect;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.jdbc.JsonAsStringJdbcType;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;

public class MariaDbJsonAsLongTextDialect extends MariaDBDialect {

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        JdbcTypeRegistry jdbcTypeRegistry = typeContributions.getTypeConfiguration().getJdbcTypeRegistry();
        jdbcTypeRegistry.addDescriptorIfAbsent(3001, JsonAsStringJdbcType.VARCHAR_INSTANCE);
        super.contributeTypes(typeContributions, serviceRegistry);
    }
}
