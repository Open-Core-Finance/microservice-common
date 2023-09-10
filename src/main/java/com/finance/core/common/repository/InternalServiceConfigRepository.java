package com.finance.core.common.repository;

import com.finance.core.common.model.AbstractInternalServiceConfig;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternalServiceConfigRepository<T extends AbstractInternalServiceConfig>
        extends CommonResourceRepository<T, String> {

    Optional<T> findFirstByApiKeyAndActivatedOrderByLastModifiedDateDesc(String apiKey, boolean activated);

    Optional<T> findFirstByApiKey(String apiKey);

}
