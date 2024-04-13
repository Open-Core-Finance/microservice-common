package tech.corefinance.common.repository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.util.CoreFinanceUtil;

@Repository
public class ResourceActionMemoryRepository extends InMemoryRepository<ResourceAction, String> implements ResourceActionRepository {

    @Autowired
    public ResourceActionMemoryRepository(CoreFinanceUtil coreFinanceUtil) {
        super(ResourceAction.class, String.class, coreFinanceUtil);
    }

    @Override
    public boolean existsByActionAndRequestMethodAndResourceTypeAndUrl(String action, RequestMethod requestMethod, String resourceType, String url) {
        return data.values().stream().anyMatch(a -> {
            var matched = true;
            matched = StringUtils.equals(a.getAction(), action);
            matched = matched && requestMethod == a.getRequestMethod();
            matched = matched && StringUtils.equals(a.getResourceType(), resourceType);
            return matched && StringUtils.equals(a.getUrl(), url);
        });
    }
}
