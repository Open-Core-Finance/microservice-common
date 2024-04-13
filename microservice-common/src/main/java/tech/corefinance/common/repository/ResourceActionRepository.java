package tech.corefinance.common.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.ResourceAction;

@NoRepositoryBean
public interface ResourceActionRepository extends CommonResourceRepository<ResourceAction, String> {

    boolean existsByActionAndRequestMethodAndResourceTypeAndUrl(String action, RequestMethod requestMethod,
                                                                String resourceType, String url);
}
