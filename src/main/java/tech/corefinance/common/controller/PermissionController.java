package tech.corefinance.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.model.AbstractPermission;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.common.service.PermissionService;

@RestController
@RequestMapping(value = "/permissions")
@ControllerManagedResource("permission")
public class PermissionController implements CrudController<String, AbstractPermission, AbstractPermission>{

    @Autowired
    private PermissionService<?, ?> permissionService;

    @Override
    public CommonService<String, AbstractPermission, ?> getHandlingService() {
        return (CommonService<String, AbstractPermission, ?>) permissionService;
    }
}
