package tech.corefinance.common.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.dto.PermissionInitializeDto;
import tech.corefinance.common.model.AbstractInternalServiceConfig;
import tech.corefinance.common.model.AbstractPermission;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.repository.PermissionRepository;

import java.io.IOException;

public interface PermissionService<T extends AbstractPermission, C extends AbstractInternalServiceConfig,
        R extends AbstractResourceAction> extends CommonService<String, T, PermissionRepository<T>>,
        InitialSupportService<String, T, PermissionRepository<T>> {

    @NotNull R newResourceAction(String resourceType, String action, String url, RequestMethod requestMethod);
}
