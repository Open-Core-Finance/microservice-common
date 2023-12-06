package tech.corefinance.userprofile.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.userprofile.entity.UserProfile;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends CommonResourceRepository<UserProfile, String> {

    Optional<UserProfile> findFirstByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);
}
