package com.finance.core.common.services;

import com.finance.core.common.dto.PermissionInitializeDto;
import com.finance.core.common.model.InternalServiceConfig;
import com.finance.core.common.model.Permission;
import com.finance.core.common.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

public interface PermissionService<T extends Permission, C extends InternalServiceConfig> extends CommonService<String, T, PermissionRepository<T>>{

    PermissionInitializeDto initializationDefaultData() throws IOException;

    T saveOrUpdatePermission(T permission);

    T createOrUpdatePermission(T permission) throws IOException;

    C saveOrUpdateApiConfig(C config);
	Page<T> loadPermission(String searchText, int pageSize, int pageIndex, List<Sort.Order> orders);
}
