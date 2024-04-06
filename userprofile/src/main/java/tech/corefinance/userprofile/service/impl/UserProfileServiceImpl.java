package tech.corefinance.userprofile.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.userprofile.dto.UserProfileCreatorDto;
import tech.corefinance.userprofile.entity.UserProfile;
import tech.corefinance.userprofile.repository.UserProfileRepository;
import tech.corefinance.userprofile.service.UserAuthenAddOn;
import tech.corefinance.userprofile.service.UserProfileService;

import java.util.List;

@Transactional
@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private List<UserAuthenAddOn> userAuthenAddOns;

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
}
