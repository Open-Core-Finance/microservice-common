package tech.corefinance.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.corefinance.common.config.InitDataConfiguration;
import tech.corefinance.common.dto.PermissionInitializeDto;
import tech.corefinance.common.model.AbstractInternalServiceConfig;
import tech.corefinance.common.model.AbstractPermission;
import tech.corefinance.common.repository.InternalServiceConfigRepository;
import tech.corefinance.common.repository.PermissionRepository;
import tech.corefinance.common.util.CoreFinanceUtil;
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
    protected PermissionRepository<T> permissionRepository;
    @Autowired
    protected InternalServiceConfigRepository<C> internalServiceConfigRepository;
    @Autowired
    protected InitDataConfiguration initDataConfiguration;
    @Autowired
    protected CoreFinanceUtil coreFinanceUtil;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected Validator validator;
    @Value(("${tech.corefinance.initial.permission-file}"))
    protected String permissionFileRegex;
    @Value(("${tech.corefinance.initial.internal-api-file}"))
    protected String internalApiFileRegex;

    @Override
    public PermissionRepository<T> getRepository() {
        return permissionRepository;
    }

    @Override
    public PermissionInitializeDto initializationDefaultData() throws IOException {
        var nameSeparator = initDataConfiguration.getNameSeparator();
        var versionSeparator = initDataConfiguration.getVersionSeparator();
        var result = new PermissionInitializeDto();
        // Internal APIs
        result.setInternalServiceConfigs(
                initialInternalApiConfigs(coreFinanceUtil.getResources(internalApiFileRegex, nameSeparator, versionSeparator)));
        // Permissions
        result.setPermissions(
                initialPermissions(coreFinanceUtil.getResources(permissionFileRegex, nameSeparator, versionSeparator)));
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
}
