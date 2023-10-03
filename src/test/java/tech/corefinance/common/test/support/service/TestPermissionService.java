package tech.corefinance.common.test.support.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.repository.PermissionRepository;
import tech.corefinance.common.service.AbstractPermissionService;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.common.service.PermissionService;
import tech.corefinance.common.test.support.model.InternalServiceConfigTest;
import tech.corefinance.common.test.support.model.PermissionTest;
import tech.corefinance.common.test.support.model.ResourceActionTest;
import tech.corefinance.common.test.support.repository.TestPermissionRepository;

@Service
@Getter
public class TestPermissionService extends AbstractPermissionService<PermissionTest, InternalServiceConfigTest, AbstractResourceAction>
    implements CommonService<String, PermissionTest, PermissionRepository<PermissionTest>> {

    @Override
    public AbstractResourceAction newResourceAction(String resourceType, String action, String url,
                                                    RequestMethod requestMethod) {
        return new ResourceActionTest(resourceType, action, url, requestMethod);
    }
}
