package tech.corefinance.common.repository;

import org.springframework.data.repository.NoRepositoryBean;
import tech.corefinance.common.model.AbstractInternalServiceConfig;

import java.util.Optional;

@NoRepositoryBean
public interface InternalServiceConfigRepository<T extends AbstractInternalServiceConfig>
        extends CommonResourceRepository<T, String> {

    Optional<T> findFirstByApiKeyAndActivatedOrderByLastModifiedDateDesc(String apiKey, boolean activated);

    Optional<T> findFirstByApiKey(String apiKey);

}
