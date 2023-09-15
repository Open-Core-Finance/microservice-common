package tech.corefinance.common.test.support.model;

import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.AbstractResourceAction;

public class ResourceActionTest extends AbstractResourceAction {
    public ResourceActionTest(String resourceType, String action, String url,
                              RequestMethod requestMethod) {
        super(resourceType, action, url, requestMethod);
    }
}
