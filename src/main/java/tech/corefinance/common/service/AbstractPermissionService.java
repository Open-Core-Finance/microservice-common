package tech.corefinance.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.corefinance.common.model.AbstractInternalServiceConfig;
import tech.corefinance.common.model.AbstractPermission;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.repository.InternalServiceConfigRepository;
import tech.corefinance.common.repository.PermissionRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract permission service that support generic permission. Need to have specific implementation for specific database type.
 * @param <T> Permission type
 * @param <C> InternalServiceConfig type
 * @param <R> ResourceAction type
 */
public abstract class AbstractPermissionService<T extends AbstractPermission, C extends AbstractInternalServiceConfig,
        R extends AbstractResourceAction> implements PermissionService<T, C, R> {

    /**
     * Repository to handle Permission.
     */
    @Autowired
    protected PermissionRepository<T> permissionRepository;
    /**
     * Repository to handle InternalServiceConfig.
     */
    @Autowired
    protected InternalServiceConfigRepository<C> internalServiceConfigRepository;
    /**
     * Context.
     */
    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * Repository to handle permission entity.
     */
    @Override
    public PermissionRepository<T> getRepository() {
        return permissionRepository;
    }
}
