package tech.corefinance.common.dto;

import tech.corefinance.common.model.InternalServiceConfig;
import tech.corefinance.common.model.Permission;
import lombok.Data;

import java.util.List;

@Data
public class PermissionInitializeDto {
    private List<? extends Permission> permissions;
    private List<? extends InternalServiceConfig> internalServiceConfigs;
}
