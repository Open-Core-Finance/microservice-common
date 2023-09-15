package tech.corefinance.common.test.support.service;

import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.service.AbstractPermissionService;
import tech.corefinance.common.test.support.model.AbstractInternalServiceConfigTest;
import tech.corefinance.common.test.support.model.PermissionTest;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tech.corefinance.common.test.support.model.ResourceActionTest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class TestPermissionService extends AbstractPermissionService<PermissionTest, AbstractInternalServiceConfigTest, AbstractResourceAction> {
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

    @Override
    public AbstractResourceAction newResourceAction(String resourceType, String action, String url,
                                                    RequestMethod requestMethod) {
        return new ResourceActionTest(resourceType, action, url, requestMethod);
    }
}
