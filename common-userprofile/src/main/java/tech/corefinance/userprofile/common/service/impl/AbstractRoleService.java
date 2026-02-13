package tech.corefinance.userprofile.common.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.userprofile.common.dto.RoleDto;
import tech.corefinance.userprofile.common.entity.CommonRole;
import tech.corefinance.userprofile.common.service.CommonRoleService;

@Service
@Transactional
@AllArgsConstructor
public abstract class AbstractRoleService<T extends CommonRole<?>> implements CommonRoleService<T> {

    @Override
    public <D extends CreateUpdateDto<String>> T copyAdditionalPropertiesFromDtoToEntity(D source, T dest) {
        dest = CommonRoleService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        if (source instanceof RoleDto roleSrc) {
            dest.setAdditionalAttributes(roleSrc.getAdditionalAttributes());
        }
        return dest;
    }
}
