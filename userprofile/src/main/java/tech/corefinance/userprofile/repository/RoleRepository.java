package tech.corefinance.userprofile.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.userprofile.common.repository.CommonRoleRepository;
import tech.corefinance.userprofile.entity.Role;

@Repository
public interface RoleRepository extends CommonRoleRepository<Role> {
}
