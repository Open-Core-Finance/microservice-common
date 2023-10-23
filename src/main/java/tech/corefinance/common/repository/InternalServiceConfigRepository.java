package tech.corefinance.common.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.common.model.InternalServiceConfig;

import java.util.Optional;

@Repository
public interface InternalServiceConfigRepository extends CommonResourceRepository<InternalServiceConfig, String> {

    Optional<InternalServiceConfig> findFirstByApiKeyAndActivatedOrderByLastModifiedDateDesc(String apiKey,
                                                                                             boolean activated);

    Optional<InternalServiceConfig> findFirstByApiKey(String apiKey);

}
