package tech.corefinance.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.dto.PageDto;
import tech.corefinance.common.model.AbstractPermission;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.service.PermissionService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/common")
@ControllerManagedResource("common")
public class CommonController {

    @Autowired
    private PermissionService<?, ?, ?> permissionService;

    @DeleteMapping(value = "/delete-permission")
    public GeneralApiResponse<Boolean> deletePermissions(@RequestParam("permisstionId") String permisstionId) throws IOException{
        return new GeneralApiResponse<>(permissionService.deleteEntity(permisstionId));
    }
    @PostMapping(value = "/create-update-permission")
    public GeneralApiResponse<AbstractPermission> createOrUpdatePermission(@RequestBody
                                                                           AbstractPermission abstractPermission) throws IOException {
        return new GeneralApiResponse<>(permissionService.createOrUpdateEntity(abstractPermission));
    }

    @PostMapping(value = "/load-permission")
    public PageDto<? extends AbstractPermission> loadPermission(@RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                                      @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                                      @RequestParam(value = "orders", required = false, defaultValue = "[]") List<Sort.Order> orders,
                                                      @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText){
        return PageDto.createSuccessResponse(permissionService.searchData(searchText, pageSize, pageIndex, orders));
    }
}
