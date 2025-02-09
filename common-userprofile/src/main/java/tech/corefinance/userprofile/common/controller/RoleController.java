package tech.corefinance.userprofile.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.userprofile.common.dto.RoleDto;
import tech.corefinance.userprofile.common.entity.CommonRole;
import tech.corefinance.userprofile.common.service.CommonRoleService;

@RestController
@RequestMapping("/roles")
@ControllerManagedResource("role")
public class RoleController implements CrudController<String, CommonRole<?>, RoleDto> {

    @SuppressWarnings("rawtypes")
    @Autowired
    private CommonRoleService commonRoleService;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public CommonRoleService getHandlingService() {
        return commonRoleService;
    }
}
