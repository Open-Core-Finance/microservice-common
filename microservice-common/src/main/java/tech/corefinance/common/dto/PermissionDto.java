package tech.corefinance.common.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.enums.AccessControl;
import tech.corefinance.common.model.Permission;
import tech.corefinance.common.model.CreateUpdateDto;

@Data
public class PermissionDto implements CreateUpdateDto<String>{
    private String id;
    private String roleId;
    private String resourceType;
    private String action;
    private String url;
    private AccessControl control;
    private RequestMethod requestMethod;

    public static PermissionDto fromPermission(Permission permission) {
        var result = new PermissionDto();
        result.setId(permission.getId());
        result.setUrl(permission.getUrl());
        result.setAction(permission.getAction());
        result.setControl(permission.getControl());
        result.setRoleId(permission.getRoleId());
        result.setRequestMethod(permission.getRequestMethod());
        result.setResourceType(permission.getResourceType());
        return result;
    }
}
