package tech.corefinance.userprofile.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.userprofile.common.dto.UserProfileCreatorDto;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;
import tech.corefinance.userprofile.common.service.UserProfileService;

@RestController
@RequestMapping("/userprofiles")
@ControllerManagedResource("userprofile")
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public class UserProfileController implements CrudController<String, CommonUserProfile<?>, UserProfileCreatorDto> {

    @SuppressWarnings("rawtypes")
    @Autowired
    private UserProfileService userProfileService;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public UserProfileService getHandlingService() {
        return userProfileService;
    }

    @PostMapping("/change-password")
    @PermissionAction(action = "change-password")
    public GeneralApiResponse<Byte> changePassword(@RequestParam("userId") String userId,
                                                   @RequestParam("currentPassword") String currentPassword,
                                                   @RequestParam("newPassword") String newPassword,
                                                   @RequestParam("confirmPassword") String confirmPassword) {
        return GeneralApiResponse.createSuccessResponse(
                userProfileService.changePassword(userId, currentPassword, newPassword, confirmPassword));
    }
}
