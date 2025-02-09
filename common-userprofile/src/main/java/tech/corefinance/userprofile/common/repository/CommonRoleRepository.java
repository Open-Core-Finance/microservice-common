package tech.corefinance.userprofile.common.repository;

import org.springframework.data.repository.NoRepositoryBean;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.userprofile.common.entity.CommonRole;

@NoRepositoryBean
public interface CommonRoleRepository<T extends CommonRole<?>> extends CommonResourceRepository<T, String> {
}
