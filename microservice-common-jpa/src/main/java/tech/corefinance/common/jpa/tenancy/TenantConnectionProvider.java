package tech.corefinance.common.jpa.tenancy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public abstract class TenantConnectionProvider implements MultiTenantConnectionProvider {

    @Autowired
    protected DataSource datasource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return datasource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        log.info("Releasing connection...");
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();

        String schema = StringUtils.lowerCase(tenantIdentifier);

        if (!isSchemaExisted(schema, connection)) {
            log.info("Creating schema [{}]", schema);
            createSchema(schema, connection);
        }

        log.info("Switching to Schema [{}]", schema);
        switchToSchema(schema, connection);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        log.info("Releasing connection of tenant [{}]", tenantIdentifier);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(@Nonnull Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(@Nonnull Class<T> unwrapType) {
        return null;
    }

    public abstract boolean isSchemaExisted(String schemaName, Connection connection) throws SQLException;

    public abstract void switchToSchema(String schemaName, Connection connection) throws SQLException;

    public abstract void createSchema(String schemaName, Connection connection) throws SQLException;
}
