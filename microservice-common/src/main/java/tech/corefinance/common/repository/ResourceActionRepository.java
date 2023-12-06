package tech.corefinance.common.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.common.model.ResourceAction;

@Repository
public interface ResourceActionRepository extends CommonResourceRepository<ResourceAction, String> {
}
