package tech.corefinance.userprofile.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
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
