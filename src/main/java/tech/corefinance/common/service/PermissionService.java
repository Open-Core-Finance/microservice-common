package tech.corefinance.common.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.AbstractInternalServiceConfig;
import tech.corefinance.common.model.AbstractPermission;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.repository.PermissionRepository;

public interface PermissionService<T extends AbstractPermission, R extends AbstractResourceAction>
        extends CommonService<String, T, PermissionRepository<T>> {

    @NotNull R newResourceAction(String resourceType, String action, String url, RequestMethod requestMethod);
}
