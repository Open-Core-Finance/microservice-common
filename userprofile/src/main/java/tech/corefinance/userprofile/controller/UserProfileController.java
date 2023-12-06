package tech.corefinance.userprofile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.userprofile.dto.UserProfileCreatorDto;
import tech.corefinance.userprofile.entity.UserProfile;
import tech.corefinance.userprofile.service.UserProfileService;

@RestController
@RequestMapping("/userprofiles")
@ControllerManagedResource("userprofile")
public class UserProfileController implements CrudController<String, UserProfile, UserProfileCreatorDto> {

    @Autowired
    private UserProfileService userProfileService;

    @Override
    public CommonService<String, UserProfile, ?> getHandlingService() {
        return userProfileService;
    }
}
