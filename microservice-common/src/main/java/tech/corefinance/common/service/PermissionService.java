package tech.corefinance.common.service;

import jakarta.validation.constraints.NotNull;
import tech.corefinance.common.entity_author.Permission;
import tech.corefinance.common.repository.PermissionRepository;

import java.util.List;

public interface PermissionService extends CommonService<String, Permission, PermissionRepository> {

    /**
     * Create Permission.
     *
     * @return New Permission object.
     */
    default @NotNull Permission createEntityObject() {
        return new Permission();
    }

    List<Permission> loadByRoles(List<String> userRoles);

    List<Permission> overrideByRoles(String roleId, List<Permission> permissions);
}
