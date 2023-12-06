package tech.corefinance.userprofile.dto;

import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;

@Data
public class RoleDto implements CreateUpdateDto<String> {
    private String entityId;
    private String name;
    private String tenantId;
}
