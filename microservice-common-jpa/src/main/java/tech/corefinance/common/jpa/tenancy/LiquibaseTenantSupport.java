package tech.corefinance.common.jpa.tenancy;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;

import java.sql.SQLException;

/**
 * Liquibase for multi-tenancies environment.
 */
public interface LiquibaseTenantSupport {

    /**
     * Execute liquibase on a specific schema.
     * @param schemaName Schema name.
     * @param springLiquibase Spring liquibase object.
     * @throws LiquibaseException When execution error.
     */
    default void executeLiquibase(String schemaName, SpringLiquibase springLiquibase) throws LiquibaseException {
        springLiquibase.setDefaultSchema(schemaName);
        springLiquibase.setShouldRun(true);
        springLiquibase.afterPropertiesSet();
    }

    /**
     * Execute liquibase on all schemas.
     * @throws SQLException When execution error.
     * @throws LiquibaseException When execution error.
     */
    void applyLiquibaseOnSchemas() throws SQLException, LiquibaseException;
}
