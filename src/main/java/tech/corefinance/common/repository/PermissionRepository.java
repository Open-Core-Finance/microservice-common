package tech.corefinance.common.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.AbstractPermission;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface PermissionRepository<T extends AbstractPermission> extends CommonResourceRepository<T, String> {

    Optional<T> findFirstByRoleIdAndResourceTypeAndActionAndUrlAndRequestMethod(String roleId, String resourceType, String action, String url, RequestMethod requestMethod);

    List<T> findAllByRoleIdAndResourceType(String roleId, String resourceType, Sort sort);

    List<T> findByRoleId(String roleId);
}
