package tech.corefinance.common.jpa.tenancy;

import jakarta.annotation.PostConstruct;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "tech.corefinance.schema-tenancy", name = "mode", havingValue = "postgresql")
public class PostgresqlLiquibaseTenantSupport implements LiquibaseTenantSupport {

    @Autowired
    private DataSource datasource;
    @Autowired
    private TenancyProperties tenancyProperties;
    @Autowired
    private SpringLiquibase springLiquibase;
    private List<String> postgresqlIgnoreSchema = new LinkedList<>();

    @PostConstruct
    public void postConstruct() throws SQLException, LiquibaseException {
        log.debug("Adding must ignore schemas...");
        postgresqlIgnoreSchema.addAll(tenancyProperties.getIgnoreSchemas());
        postgresqlIgnoreSchema.addAll(List.of("pg_toast", "pg_catalog", "information_schema"));
        log.debug("Finalize list schema to ignore liquibase {}", postgresqlIgnoreSchema);
        applyLiquibaseOnSchemas();
    }

    @Override
    public void applyLiquibaseOnSchemas() throws SQLException, LiquibaseException {
        try (Connection connection = datasource.getConnection()) {
            log.debug("Finding list schema to apply liquibase....");
            var sqlBuilder = new StringBuilder("SELECT schema_name FROM information_schema.schemata where schema_name not in (");
            var index = 0;
            for (var schemaName : postgresqlIgnoreSchema) {
                if (index++ > 0) {
                    sqlBuilder.append(",");
                }
                sqlBuilder.append("'").append(schemaName).append("'");
            }
            sqlBuilder.append(")");
            var sql = sqlBuilder.toString();
            log.debug("SQL [{}]", sql);
            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                var schemaName = rs.getString("schema_name");
                log.debug("=================================");
                log.debug("Applying liquibase in [{}]", schemaName);
                executeLiquibase(schemaName, springLiquibase);
                log.debug("Applying liquibase in [{}] done!", schemaName);
                log.debug("=================================");
            }
        }
    }
}
