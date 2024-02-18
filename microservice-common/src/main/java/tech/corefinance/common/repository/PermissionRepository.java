package tech.corefinance.common.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.Permission;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends CommonResourceRepository<Permission, String> {

    Optional<Permission> findFirstByRoleIdAndResourceTypeAndActionAndUrlAndRequestMethod(String roleId, String resourceType, String action, String url, RequestMethod requestMethod);

    List<Permission> findAllByRoleIdAndResourceType(String roleId, String resourceType, Sort sort);

    List<Permission> findByRoleIdIn(Iterable<String> roleIds);

    void deleteAllByRoleIdIn(Iterable<? extends String> roleIds);
}
