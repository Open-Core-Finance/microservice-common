package tech.corefinance.common.jpa.tenancy;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "tech.corefinance.schema-tenancy", name = "mode", havingValue = "postgresql")
public class PostgresqlTenantConnectionProvider extends TenantConnectionProvider {

    @Value("${spring.liquibase.enabled}")
    private boolean liquibaseEnabled;
    @Autowired(required = false)
    private LiquibaseTenantSupport liquibaseTenantSupport;
    @Autowired
    private SpringLiquibase springLiquibase;

    @Override
    public boolean isSchemaExisted(String schemaName, Connection connection) throws SQLException {
        var sql = new StringBuilder("SELECT schema_name FROM information_schema.schemata WHERE schema_name = '")
                .append(schemaName).append("'").toString();
        log.debug("SQL [{}]", sql);
        return connection.createStatement().executeQuery(sql).next();
    }

    @Override
    public void switchToSchema(String schemaName, Connection connection) throws SQLException {
        var sql = new StringBuilder("SET Schema '").append(schemaName).append("'").toString();
        log.debug("SQL [{}]", sql);
        connection.createStatement().execute(sql);
    }

    @Override
    public void createSchema(String schemaName, Connection connection) throws SQLException {
        var sql = new StringBuilder("CREATE SCHEMA IF NOT EXISTS \"").append(schemaName).append("\"").toString();
        log.debug("SQL [{}]", sql);
        connection.createStatement().execute(sql);
        if (liquibaseTenantSupport != null) {
            try {
                liquibaseTenantSupport.executeLiquibase(schemaName, springLiquibase);
            } catch (LiquibaseException e) {
                throw new SQLException(e);
            }
        }
    }
}
