package com.finance.core.common.service;

import com.finance.core.common.dto.JwtTokenDto;
import com.finance.core.common.dto.UserRoleDto;
import com.finance.core.common.model.Permission;

public interface ResourceOwnerVerifier {

    boolean verifyOwner(JwtTokenDto jwtTokenDto, String resourceType, Object resourceId, Permission permission,
                        UserRoleDto userRole);

    boolean isSupportedResource(String resourceType);

}
