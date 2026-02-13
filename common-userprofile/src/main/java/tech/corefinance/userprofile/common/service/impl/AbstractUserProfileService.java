package tech.corefinance.userprofile.common.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.service.JwtService;
import tech.corefinance.userprofile.common.dto.UserProfileCreatorDto;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;
import tech.corefinance.userprofile.common.service.UserProfileService;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public abstract class AbstractUserProfileService<T extends CommonUserProfile<?>> implements UserProfileService<T> {

    protected PasswordEncoder passwordEncoder;
    protected JwtService jwtService;

    @Override
    public <D extends CreateUpdateDto<String>> T copyAdditionalPropertiesFromDtoToEntity(D source, T dest) {
        dest = UserProfileService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        if (source instanceof UserProfileCreatorDto creatorDto) {
            dest.setAdditionalAttributes(creatorDto.getAdditionalAttributes());
        }
        return dest;
    }

    @Override
    public byte changePassword(String userId, String currentPassword, String newPassword, String repeatNewPassword) {
        var userProfile = getEntityDetails(userId);
        var userPass = userProfile.getPassword();
        if (userPass != null && !passwordEncoder.matches(currentPassword, userPass)) {
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

    public byte selfUpdatePassword(String currentPassword, String newPassword, String repeatNewPassword) {
        JwtContext jwtContext = JwtContext.getInstance();
        BasicUserDto userDto = jwtService.retrieveUserAsAttribute(jwtContext.getJwt());
        return changePassword(userDto.getUserId(), currentPassword, newPassword, repeatNewPassword);
    }
}
