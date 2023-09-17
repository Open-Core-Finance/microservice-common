package tech.corefinance.common.test.support.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.service.AbstractPermissionService;
import tech.corefinance.common.service.LocalResourceEntityInitializer;
import tech.corefinance.common.test.support.model.InternalServiceConfigTest;
import tech.corefinance.common.test.support.model.PermissionTest;
import tech.corefinance.common.test.support.model.ResourceActionTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
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

    protected Map<String, LocalResourceEntityInitializer<? extends Object>> listInitialNamesSupported;

    public TestPermissionService() {
        listInitialNamesSupported = new LinkedHashMap<>();
        listInitialNamesSupported.put("permission", new LocalResourceEntityInitializer<>(
                new TypeReference<List<PermissionTest>>() {},
        (entity, overrideIfExisted) -> initPermission(entity, overrideIfExisted)));
        listInitialNamesSupported.put("internal-api", new LocalResourceEntityInitializer<>(
                new TypeReference<List<InternalServiceConfigTest>>() {},
                (entity, overrideIfExisted) -> initApiConfig(entity, overrideIfExisted)));
    }
}
