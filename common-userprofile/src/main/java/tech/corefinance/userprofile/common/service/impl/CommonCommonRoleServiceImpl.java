package tech.corefinance.userprofile.common.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.userprofile.common.dto.RoleDto;
import tech.corefinance.userprofile.common.entity.CommonRole;
import tech.corefinance.userprofile.common.repository.CommonRoleRepository;
import tech.corefinance.userprofile.common.service.CommonRoleService;
import tech.corefinance.userprofile.common.service.UserAuthenAddOn;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Service
@Transactional
@AllArgsConstructor
@ConditionalOnProperty(prefix = "tech.corefinance.app.userprofile", name = "common-role-service", havingValue = "true",
        matchIfMissing = true)
public class CommonCommonRoleServiceImpl implements CommonRoleService<CommonRole<?>> {

    private final CommonRoleRepository commonRoleRepository;
    private final List<UserAuthenAddOn> userAuthenAddOns;

    @Override
    public CommonRoleRepository getRepository() {
        return commonRoleRepository;
    }

    private UserAuthenAddOn getSuitableUserAuthenAddOn() {
        return this.userAuthenAddOns.getFirst();
    }

    @Override
    public <D extends CreateUpdateDto<String>> CommonRole<?> copyAdditionalPropertiesFromDtoToEntity(D source, CommonRole<?> dest) {
        dest = CommonRoleService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        if (source instanceof RoleDto roleSrc) {
            dest.setAdditionalAttributes(roleSrc.getAdditionalAttributes());
        }
        return dest;
    }
}
