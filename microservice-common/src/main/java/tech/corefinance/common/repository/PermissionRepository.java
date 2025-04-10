package tech.corefinance.common.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.entity_author.Permission;

import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public interface PermissionRepository extends CommonResourceRepository<Permission, String> {

    Optional<Permission> findFirstByRoleIdAndResourceTypeAndActionAndUrlAndRequestMethod(String roleId, String resourceType, String action, String url, RequestMethod requestMethod);

    List<Permission> findAllByRoleIdAndResourceType(String roleId, String resourceType, Sort sort);

    List<Permission> findByRoleIdIn(Iterable<String> roleIds);

    void deleteAllByRoleIdIn(Iterable<? extends String> roleIds);
}
