package tech.corefinance.common.dto;

import lombok.Data;
import tech.corefinance.common.entity_author.InternalServiceConfig;
import tech.corefinance.common.entity_author.Permission;

import java.util.List;

@Data
public class PermissionInitializeDto {
    private List<? extends Permission> permissions;
    private List<? extends InternalServiceConfig> internalServiceConfigs;
}
