package tech.corefinance.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.dto.PermissionDto;
import tech.corefinance.common.model.Permission;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.common.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping(value = "/permissions")
@ControllerManagedResource("permission")
public class PermissionController implements CrudController<String, Permission, PermissionDto>{

    @Autowired
    private PermissionService permissionService;

    @Override
    public CommonService<String, Permission, ?> getHandlingService() {
        return permissionService;
    }

    @PostMapping("/load-by-roles")
    @PermissionAction(action = "load_by_role")
    public GeneralApiResponse<List<Permission>> loadByRoles(@RequestBody List<String> roleIds) {
        return GeneralApiResponse.createSuccessResponse(permissionService.loadByRoles(roleIds));
    }

    @PutMapping("/override-by-roles/{roleId}")
    @PermissionAction(action = "override_by_role")
    public GeneralApiResponse<List<Permission>> overrideByRoles(@PathVariable("roleId") String roleId,
                                                                @RequestBody List<Permission> permissions) {
        return GeneralApiResponse.createSuccessResponse(permissionService.overrideByRoles(roleId, permissions));
    }
}
