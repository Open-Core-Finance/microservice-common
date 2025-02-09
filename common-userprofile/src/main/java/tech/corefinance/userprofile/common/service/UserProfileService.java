package tech.corefinance.userprofile.common.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;
import tech.corefinance.userprofile.common.repository.CommonUserProfileRepository;

public interface UserProfileService<T extends CommonUserProfile<?>> extends CommonService<String, T, CommonUserProfileRepository<T>> {
    byte changePassword(String userId, String currentPassword, String newPassword, String repeatNewPassword);
}
