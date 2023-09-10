package com.finance.core.common.test.support.service;

import com.finance.core.common.service.AbstractPermissionService;
import com.finance.core.common.test.support.model.AbstractInternalServiceConfigTest;
import com.finance.core.common.test.support.model.PermissionTest;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class TestPermissionService extends AbstractPermissionService<PermissionTest, AbstractInternalServiceConfigTest> {
    @Override
    protected List<PermissionTest> initialPermissions(List<Resource> permissionResources) throws IOException {
        return new LinkedList<>();
    }

    @Override
    protected List<AbstractInternalServiceConfigTest> initialInternalApiConfigs(List<Resource> configResources)
            throws IOException {
        return new LinkedList<>();
    }

    @Override
    public PermissionTest newPermission() {
        return new PermissionTest();
    }
}
