package tech.corefinance.common.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.common.model.AnonymousUrlAccess;

@Repository
public interface AnonymousUrlAccessRepository extends CommonResourceRepository<AnonymousUrlAccess, String> {
}
