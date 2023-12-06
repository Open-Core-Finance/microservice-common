package tech.corefinance.common.mongodb.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.corefinance.common.model.InternalServiceConfig;
import tech.corefinance.common.model.Permission;
import tech.corefinance.common.repository.InternalServiceConfigRepository;
import tech.corefinance.common.repository.PermissionRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissionInitialService implements InitialSupportService {

    protected Map<String, LocalResourceEntityInitializer<? extends Object>> listInitialNamesSupported;

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private InternalServiceConfigRepository internalServiceConfigRepository;

    public PermissionInitialService() {
        listInitialNamesSupported = new LinkedHashMap<>();
        listInitialNamesSupported.put("permission", new LocalResourceEntityInitializer<>(
                new TypeReference<List<Permission>>() {},
                (entity, overrideIfExisted) -> initPermission(entity, overrideIfExisted)));
        listInitialNamesSupported.put("internal-api", new LocalResourceEntityInitializer<>(
                new TypeReference<List<InternalServiceConfig>>() {},
                (entity, overrideIfExisted) -> initApiConfig(entity, overrideIfExisted)));
    }

    /**
     * Init permission info. Replace if existed.
     *
     * @param permission Permission to initial.
     * @param overrideIfExisted Override if existed in DB.
     * @return Permission saved in DB.
     */
    protected Permission initPermission(Permission permission, boolean overrideIfExisted) {
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
     * @param overrideIfExisted Override if existed in DB.
     * @return API Config saved in DB.
     */
    protected InternalServiceConfig initApiConfig(InternalServiceConfig config, boolean overrideIfExisted) {
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
    public Map<String, LocalResourceEntityInitializer<? extends Object>> getListInitialNamesSupported() {
        return listInitialNamesSupported;
    }
}
