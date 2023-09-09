package com.finance.core.common.repository;

import com.finance.core.common.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository<T extends Permission> extends CommonResourceRepository<T, String> {

    Optional<T> findFirstByRoleIdAndResourceTypeAndActionAndUrlAndRequestMethod(String roleId, String resourceType, String action, String url, RequestMethod requestMethod);

    List<T> findAllByRoleIdAndResourceType(String roleId, String resourceType, Sort sort);

    // TODO need to override
    Page<T> searchBy(@Param("search") String searchText, Pageable pageRequest);

}
