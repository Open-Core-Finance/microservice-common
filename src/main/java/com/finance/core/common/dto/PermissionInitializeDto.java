package com.finance.core.common.dto;

import com.finance.core.common.model.AbstractInternalServiceConfig;
import com.finance.core.common.model.AbstractPermission;
import lombok.Data;

import java.util.List;

@Data
public class PermissionInitializeDto {
    private List<? extends AbstractPermission> permissions;
    private List<? extends AbstractInternalServiceConfig> internalServiceConfigs;
}
