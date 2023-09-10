package com.finance.core.common.service;

import com.finance.core.common.dto.JwtTokenDto;
import com.finance.core.common.dto.UserRoleDto;
import com.finance.core.common.model.AbstractPermission;

public interface ResourceOwnerVerifier {

    boolean verifyOwner(JwtTokenDto jwtTokenDto, String resourceType, Object resourceId, AbstractPermission abstractPermission,
                        UserRoleDto userRole);

    boolean isSupportedResource(String resourceType);

}
