package com.finance.core.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.core.common.config.InitDataConfiguration;
import com.finance.core.common.dto.PermissionInitializeDto;
import com.finance.core.common.model.AbstractInternalServiceConfig;
import com.finance.core.common.model.AbstractPermission;
import com.finance.core.common.util.Util;
import com.finance.core.common.repository.InternalServiceConfigRepository;
import com.finance.core.common.repository.PermissionRepository;
import jakarta.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractPermissionService<T extends AbstractPermission, C extends AbstractInternalServiceConfig>
        implements PermissionService<T, C> {

    @Autowired
    private PermissionRepository<T> permissionRepository;
    @Autowired
    private InternalServiceConfigRepository<C> internalServiceConfigRepository;
    @Autowired
    private InitDataConfiguration initDataConfiguration;
    @Autowired
    private Util util;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Validator validator;
    @Value(("${com.finance.core.initial.permission-file}"))
    private String permissionFileRegex;
    @Value(("${com.finance.core.initial.internal-api-file}"))
    private String internalApiFileRegex;

    @Override
    public PermissionRepository getRepository() {
        return permissionRepository;
    }

    @Override
    public PermissionInitializeDto initializationDefaultData() throws IOException {
        var nameSeparator = initDataConfiguration.getNameSeparator();
        var versionSeparator = initDataConfiguration.getVersionSeparator();
        var result = new PermissionInitializeDto();
        // Internal APIs
        result.setInternalServiceConfigs(
                initialInternalApiConfigs(util.getResources(internalApiFileRegex, nameSeparator, versionSeparator)));
        // Permissions
        result.setPermissions(
                initialPermissions(util.getResources(permissionFileRegex, nameSeparator, versionSeparator)));
        // Return
        return result;
    }

    @Override
    public T saveOrUpdatePermission(T permission) {
        var optional = permissionRepository.findFirstByRoleIdAndResourceTypeAndActionAndUrlAndRequestMethod(
                permission.getRoleId(),
                permission.getResourceType(), permission.getAction(), permission.getUrl(),
                permission.getRequestMethod());
        if (!optional.isPresent()) {
            return permissionRepository.save(permission);
        } else {
            var per = optional.get();
            per.setControl(permission.getControl());
            return permissionRepository.save(per);
        }
    }

    @Override
    public C saveOrUpdateApiConfig(C config) {
        var optional = internalServiceConfigRepository.findFirstByApiKey(config.getApiKey());
        if (!optional.isPresent()) {
            return internalServiceConfigRepository.save(config);
        } else {
            var per = optional.get();
            per.setServiceName(config.getServiceName());
            per.setActivated(config.isActivated());
            return internalServiceConfigRepository.save(per);
        }
    }

    protected abstract List<T> initialPermissions(List<Resource> permissionResources) throws IOException;
    protected abstract List<C> initialInternalApiConfigs(List<Resource> configResources) throws IOException;

    @Override
    public T createOrUpdatePermission(T permission) throws IOException {
        T dbPermission;
        if (permission.getId() != null) {
            Optional<T> optional = permissionRepository.findById(permission.getId());
            if (optional.isPresent()) {
                dbPermission = optional.get();
            } else {
                dbPermission = newPermission();
            }
            dbPermission.setId(permission.getId());
        } else {
            dbPermission = newPermission();
        }
        validator.validate(permission);
        dbPermission.setRoleId(permission.getRoleId());
        dbPermission.setResourceType(permission.getResourceType());
        dbPermission.setAction(permission.getAction());
        dbPermission.setUrl(permission.getUrl());
        dbPermission.setControl(permission.getControl());
        dbPermission.setRequestMethod(permission.getRequestMethod());
        permissionRepository.save(dbPermission);
        return dbPermission;
    }

    @Override
    public Page<T> loadPermission(String searchText, int pageSize, int pageIndex, List<Sort.Order> orders) {
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by(orders));
        if (StringUtils.isBlank(searchText)) {
            return permissionRepository.findAll(pageRequest);
        } else {
            return permissionRepository.searchBy(searchText, pageRequest);
        }
    }

    public abstract T newPermission();
}
