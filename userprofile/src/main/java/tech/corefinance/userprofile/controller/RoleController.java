package tech.corefinance.userprofile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.userprofile.dto.RoleDto;
import tech.corefinance.userprofile.dto.UserProfileCreatorDto;
import tech.corefinance.userprofile.entity.Role;
import tech.corefinance.userprofile.entity.UserProfile;
import tech.corefinance.userprofile.repository.RoleRepository;
import tech.corefinance.userprofile.service.UserProfileService;

@RestController
@RequestMapping("/roles")
@ControllerManagedResource("role")
public class RoleController implements CrudServiceAndController<String, Role, RoleDto, RoleRepository> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleRepository getRepository() {
        return roleRepository;
    }
}
