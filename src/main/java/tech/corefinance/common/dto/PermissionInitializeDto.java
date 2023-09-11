package tech.corefinance.common.dto;

import tech.corefinance.common.model.AbstractInternalServiceConfig;
import tech.corefinance.common.model.AbstractPermission;
import lombok.Data;

import java.util.List;

@Data
public class PermissionInitializeDto {
    private List<? extends AbstractPermission> permissions;
    private List<? extends AbstractInternalServiceConfig> internalServiceConfigs;
}
