package tech.corefinance.userprofile.common.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.userprofile.common.entity.CommonRole;
import tech.corefinance.userprofile.common.repository.CommonRoleRepository;

public interface CommonRoleService<T extends CommonRole<?>> extends CommonService<String, T, CommonRoleRepository<T>> {
}
