package tech.corefinance.userprofile.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.userprofile.entity.Role;
import tech.corefinance.userprofile.repository.RoleRepository;

public interface RoleService extends CommonService<String, Role, RoleRepository> {
}
