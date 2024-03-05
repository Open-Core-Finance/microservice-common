package tech.corefinance.userprofile.dto;

import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;

import java.util.Map;

@Data
public class RoleDto implements CreateUpdateDto<String> {
    private String id;
    private String name;
    private String tenantId;
    private Map<String, Object> additionalAttributes;
}
