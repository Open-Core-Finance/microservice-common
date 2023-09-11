package tech.corefinance.common.service;

import jakarta.validation.constraints.NotNull;
import tech.corefinance.common.dto.PermissionInitializeDto;
import tech.corefinance.common.model.AbstractInternalServiceConfig;
import tech.corefinance.common.model.AbstractPermission;
import tech.corefinance.common.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

public interface PermissionService<T extends AbstractPermission, C extends AbstractInternalServiceConfig> extends CommonService<String, T, PermissionRepository<T>>{

    PermissionInitializeDto initializationDefaultData() throws IOException;

    T saveOrUpdatePermission(T permission);

    T createOrUpdatePermission(T permission) throws IOException;

    C saveOrUpdateApiConfig(C config);
	Page<T> loadPermission(String searchText, int pageSize, int pageIndex, List<Sort.Order> orders);

    @NotNull T newPermission();
}
