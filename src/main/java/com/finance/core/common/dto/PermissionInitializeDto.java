package com.finance.core.common.dto;

import com.finance.core.common.model.InternalServiceConfig;
import com.finance.core.common.model.Permission;
import lombok.Data;

import java.util.List;

@Data
public class PermissionInitializeDto {
    private List<? extends Permission> permissions;
    private List<? extends InternalServiceConfig> internalServiceConfigs;
}
