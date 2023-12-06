package tech.corefinance.userprofile.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.userprofile.entity.UserProfile;
import tech.corefinance.userprofile.repository.UserProfileRepository;

public interface UserProfileService extends CommonService<String, UserProfile, UserProfileRepository> {
}
