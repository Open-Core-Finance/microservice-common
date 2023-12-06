package tech.corefinance.common.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.repository.PermissionRepository;

/**
 * JPA permission service.
 */
@Service
@Transactional
@Getter
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public PermissionRepository getRepository() {
        return permissionRepository;
    }

}
