package tech.corefinance.common.service;

import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.dto.UserRoleDto;
import tech.corefinance.common.model.AbstractPermission;

public interface ResourceOwnerVerifier {

    boolean verifyOwner(JwtTokenDto jwtTokenDto, String resourceType, Object resourceId, AbstractPermission abstractPermission,
                        UserRoleDto userRole);

    boolean isSupportedResource(String resourceType);

}
