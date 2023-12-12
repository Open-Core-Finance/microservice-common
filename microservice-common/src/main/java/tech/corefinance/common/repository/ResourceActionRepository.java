package tech.corefinance.common.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.ResourceAction;

@Repository
public interface ResourceActionRepository extends CommonResourceRepository<ResourceAction, String> {

    boolean existsByActionAndRequestMethodAndResourceTypeAndUrl(String action, RequestMethod requestMethod,
                                                                String resourceType, String url);
}
