package tech.corefinance.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.corefinance.common.model.AbstractInternalServiceConfig;
import tech.corefinance.common.model.AbstractPermission;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.repository.InternalServiceConfigRepository;
import tech.corefinance.common.repository.PermissionRepository;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractPermissionService<T extends AbstractPermission, C extends AbstractInternalServiceConfig,
        R extends AbstractResourceAction> implements PermissionService<T, C, R> {

    @Autowired
    protected PermissionRepository<T> permissionRepository;
    @Autowired
    protected InternalServiceConfigRepository<C> internalServiceConfigRepository;
    @Autowired
    protected ObjectMapper objectMapper;

    protected Map<String, EntityInitializer<? extends Object>> listInitialNamesSupported;

    public AbstractPermissionService() {
        listInitialNamesSupported = new LinkedHashMap<>();
        listInitialNamesSupported.put("permission",
                (T permission, boolean overrideIfExisted) -> initPermission(permission, overrideIfExisted));
        listInitialNamesSupported.put("internal-api",
                (C apiConfig, boolean overrideIfExisted) -> initApiConfig(apiConfig, overrideIfExisted));
    }

    @Override
    public PermissionRepository<T> getRepository() {
        return permissionRepository;
    }

    /**
     * Init permission info. Replace if existed.
     *
     * @param permission Permission to initial.
     * @return Permission saved in DB.
     */
    public T initPermission(T permission, boolean overrideIfExisted) {
        var optional = permissionRepository.findFirstByRoleIdAndResourceTypeAndActionAndUrlAndRequestMethod(
                permission.getRoleId(),
                permission.getResourceType(), permission.getAction(), permission.getUrl(),
                permission.getRequestMethod());
        if (!optional.isPresent()) {
            return permissionRepository.save(permission);
        } else {
            var per = optional.get();
            if (overrideIfExisted) {
                per.setControl(permission.getControl());
                per = permissionRepository.save(per);
            }
            return per;
        }
    }

    /**
     * Init API Config info. Replace if existed.
     *
     * @param config API Config to initial.
     * @return API Config saved in DB.
     */
    public C initApiConfig(C config, boolean overrideIfExisted) {
        var optional = internalServiceConfigRepository.findFirstByApiKey(config.getApiKey());
        if (!optional.isPresent()) {
            return internalServiceConfigRepository.save(config);
        } else {
            var per = optional.get();
            if (overrideIfExisted) {
                per.setServiceName(config.getServiceName());
                per.setActivated(config.isActivated());
                per = internalServiceConfigRepository.save(per);
            }
            return per;
        }
    }

    @Override
    public Map<String, EntityInitializer<? extends Object>> getListInitialNamesSupported() {
        return listInitialNamesSupported;
    }
}
