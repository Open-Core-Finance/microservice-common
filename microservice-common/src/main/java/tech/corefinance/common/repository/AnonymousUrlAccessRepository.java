package tech.corefinance.common.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.entity_author.AnonymousUrlAccess;

@Repository
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public interface AnonymousUrlAccessRepository extends CommonResourceRepository<AnonymousUrlAccess, String> {
}
