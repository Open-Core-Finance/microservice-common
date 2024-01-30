package tech.corefinance.userprofile.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.userprofile.dto.RoleDto;
import tech.corefinance.userprofile.entity.Role;
import tech.corefinance.userprofile.repository.RoleRepository;
import tech.corefinance.userprofile.service.RoleService;
import tech.corefinance.userprofile.service.UserAuthenAddOn;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private List<UserAuthenAddOn> userAuthenAddOns;

    @Override
    public RoleRepository getRepository() {
        return roleRepository;
    }

    private UserAuthenAddOn getSuitableUserAuthenAddOn() {
        return this.userAuthenAddOns.iterator().next();
    }

    @Override
    public <D extends CreateUpdateDto<String>> void copyAdditionalPropertiesFromDtoToEntity(D source, Role dest) {
        RoleService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        if (source instanceof RoleDto roleSrc) {
            dest.setAdditionalAttributes(roleSrc.getAdditionalAttributes());
        }
    }
}
