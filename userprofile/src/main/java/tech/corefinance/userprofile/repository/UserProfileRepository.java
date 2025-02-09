package tech.corefinance.userprofile.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.userprofile.common.repository.CommonUserProfileRepository;
import tech.corefinance.userprofile.entity.UserProfile;

@Repository
public interface UserProfileRepository extends CommonUserProfileRepository<UserProfile> {
}
