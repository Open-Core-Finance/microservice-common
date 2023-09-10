package com.finance.core.common.controller;

import com.finance.core.common.annotation.ControllerManagedResource;
import com.finance.core.common.annotation.PermissionAction;
import com.finance.core.common.dto.GeneralApiResponse;
import com.finance.core.common.dto.PageDto;
import com.finance.core.common.dto.PermissionInitializeDto;
import com.finance.core.common.model.AbstractPermission;
import com.finance.core.common.model.ResourceAction;
import com.finance.core.common.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/common")
@ControllerManagedResource("common")
public class CommonController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping(value = "/initialization-default-permissions-data")
    @PermissionAction(action = ResourceAction.COMMON_ACTION_INITIAL)
    public GeneralApiResponse<PermissionInitializeDto> initializationDefaultData() throws IOException {
        return new GeneralApiResponse<>(permissionService.initializationDefaultData());
    }
    @DeleteMapping(value = "/delete-permission")
    public GeneralApiResponse<Boolean> deletePermissions(@RequestParam("permisstionId") String permisstionId) throws IOException{
        return new GeneralApiResponse<>(permissionService.deleteRecord(permisstionId));
    }
    @PostMapping(value = "/create-update-permission")
    public GeneralApiResponse<AbstractPermission> createOrUpdatePermission(@RequestBody
                                                                           AbstractPermission abstractPermission) throws IOException {
        return new GeneralApiResponse<>(permissionService.createOrUpdatePermission(abstractPermission));
    }

    @PostMapping(value = "/load-permission")
    public PageDto<AbstractPermission> loadPermission(@RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                                      @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                                      @RequestParam(value = "orders", required = false, defaultValue = "[]") List<Sort.Order> orders,
                                                      @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText){
        return PageDto.createSuccessResponse(permissionService.loadPermission(searchText, pageSize, pageIndex, orders));
    }
}
