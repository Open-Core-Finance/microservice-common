package tech.corefinance.userprofile.common.repository;

import org.springframework.data.repository.NoRepositoryBean;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;

import java.util.Optional;

@NoRepositoryBean
public interface CommonUserProfileRepository<T extends CommonUserProfile> extends CommonResourceRepository<T, String> {

    Optional<T> findFirstByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);
}
