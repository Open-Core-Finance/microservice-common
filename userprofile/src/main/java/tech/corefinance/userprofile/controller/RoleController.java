package tech.corefinance.userprofile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.userprofile.dto.RoleDto;
import tech.corefinance.userprofile.entity.Role;
import tech.corefinance.userprofile.service.RoleService;

@RestController
@RequestMapping("/roles")
@ControllerManagedResource("role")
public class RoleController implements CrudController<String, Role, RoleDto> {

    @Autowired
    private RoleService roleService;

    @Override
    public CommonService<String, Role, ?> getHandlingService() {
        return roleService;
    }
}
