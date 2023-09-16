package tech.corefinance.common.test.support.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.service.AbstractPermissionService;
import tech.corefinance.common.test.support.model.InternalServiceConfigTest;
import tech.corefinance.common.test.support.model.PermissionTest;
import tech.corefinance.common.test.support.model.ResourceActionTest;

import java.util.List;

@Service
public class TestPermissionService extends AbstractPermissionService<PermissionTest, InternalServiceConfigTest, AbstractResourceAction> {

    @Override
    public PermissionTest createEntityObject() {
        return new PermissionTest();
    }

    @Override
    public AbstractResourceAction newResourceAction(String resourceType, String action, String url,
                                                    RequestMethod requestMethod) {
        return new ResourceActionTest(resourceType, action, url, requestMethod);
    }

    @Override
    protected TypeReference<List<PermissionTest>> getPermissionJsonParseType() {
        return new TypeReference<>() {
        };
    }

    @Override
    protected TypeReference<List<InternalServiceConfigTest>> getApiConfigJsonParseType() {
        return new TypeReference<>() {
        };
    }
}
