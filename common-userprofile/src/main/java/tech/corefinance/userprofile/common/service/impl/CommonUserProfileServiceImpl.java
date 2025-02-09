package tech.corefinance.userprofile.common.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.userprofile.common.dto.UserProfileCreatorDto;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;
import tech.corefinance.userprofile.common.repository.CommonUserProfileRepository;
import tech.corefinance.userprofile.common.service.UserAuthenAddOn;
import tech.corefinance.userprofile.common.service.UserProfileService;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Transactional
@Service
@Slf4j
@AllArgsConstructor
@ConditionalOnProperty(prefix = "tech.corefinance.app.userprofile", name = "common-user-service", havingValue = "true",
        matchIfMissing = true)
public class CommonUserProfileServiceImpl implements UserProfileService<CommonUserProfile<?>> {

    private CommonUserProfileRepository commonUserProfileRepository;
    private List<UserAuthenAddOn> userAuthenAddOns;
    private PasswordEncoder passwordEncoder;

    @Override
    public CommonUserProfileRepository getRepository() {
        return commonUserProfileRepository;
    }

    private UserAuthenAddOn getSuitableUserAuthenAddOn() {
        return this.userAuthenAddOns.getFirst();
    }

    @Override
    public <D extends CreateUpdateDto<String>> CommonUserProfile<?> copyAdditionalPropertiesFromDtoToEntity(D source,
                                                                                                            CommonUserProfile<?> dest) {
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
