package tech.corefinance.common.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.entity_author.InternalServiceConfig;

import java.util.Optional;

@Repository
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public interface InternalServiceConfigRepository extends CommonResourceRepository<InternalServiceConfig, String> {

    Optional<InternalServiceConfig> findFirstByApiKeyAndActivatedOrderByLastModifiedDateDesc(String apiKey,
                                                                                             boolean activated);

    Optional<InternalServiceConfig> findFirstByApiKey(String apiKey);

}
