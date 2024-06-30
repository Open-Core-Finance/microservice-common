package tech.corefinance.userprofile.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.userprofile.dto.UserProfileCreatorDto;
import tech.corefinance.userprofile.entity.UserProfile;
import tech.corefinance.userprofile.repository.UserProfileRepository;
import tech.corefinance.userprofile.service.UserAuthenAddOn;
import tech.corefinance.userprofile.service.UserProfileService;

import java.util.List;

@Transactional
@Service
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private List<UserAuthenAddOn> userAuthenAddOns;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserProfileRepository getRepository() {
        return userProfileRepository;
    }

    private UserAuthenAddOn getSuitableUserAuthenAddOn() {
        return this.userAuthenAddOns.iterator().next();
    }

    @Override
    public <D extends CreateUpdateDto<String>> UserProfile copyAdditionalPropertiesFromDtoToEntity(D source, UserProfile dest) {
        dest = UserProfileService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        if (source instanceof UserProfileCreatorDto creatorDto) {
            dest.setAdditionalAttributes(creatorDto.getAdditionalAttributes());
        }
        return dest;
    }

    @Override
    public byte changePassword(String userId, String currentPassword, String newPassword, String repeatNewPassword) {
        var userProfile = getEntityDetails(userId);
        if (!passwordEncoder.matches(currentPassword, userProfile.getPassword())) {
            log.debug("Current password is incorrect");
            return -1;
        }
        if (!StringUtils.hasText(newPassword) || !newPassword.equals(repeatNewPassword)) {
            log.debug("New password and repeat new password does not match");
            return -2;
        }
        var encodedPassword = passwordEncoder.encode(newPassword);
        log.debug("Setting new encoded password [{}]", encodedPassword);
        userProfile.setPassword(encodedPassword);
        return 0;
    }
}
