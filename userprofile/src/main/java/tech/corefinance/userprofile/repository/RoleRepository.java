package tech.corefinance.userprofile.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.userprofile.entity.Role;

@Repository
public interface RoleRepository extends CommonResourceRepository<Role, String> {
}
