package tech.corefinance.common.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.entity_author.Permission;
import tech.corefinance.common.repository.PermissionRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * JPA permission service.
 */
@Service
@Transactional
@Getter
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public PermissionRepository getRepository() {
        return permissionRepository;
    }

    @Override
    public List<Permission> loadByRoles(List<String> roleIds) {
        return permissionRepository.findByRoleIdIn(roleIds);
    }

    @Override
    public List<Permission> overrideByRoles(String roleId, List<Permission> permissions) {
        permissionRepository.deleteAllByRoleIdIn(List.of(roleId));
        List<Permission> permissionsToSave = new ArrayList<>(permissions.size());
        List<Permission> permissionsIgnored = new LinkedList<>();
        for (var p : permissions) {
            p.setRoleId(roleId);
            var optional = permissionRepository.findFirstByRoleIdAndResourceTypeAndActionAndUrlAndRequestMethod(
                    p.getRoleId(), p.getResourceType(), p.getAction(), p.getUrl(), p.getRequestMethod()
            );
            optional.ifPresentOrElse(per -> permissionsIgnored.add(per), () -> permissionsToSave.add(p));
        }
        var saved = permissionRepository.saveAll(permissionsToSave);
        permissionsIgnored.addAll(saved);
        return permissionsIgnored;
    }
}
