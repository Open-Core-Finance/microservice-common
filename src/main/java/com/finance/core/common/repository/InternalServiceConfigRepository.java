package com.finance.core.common.repository;

import com.finance.core.common.model.InternalServiceConfig;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternalServiceConfigRepository<T extends InternalServiceConfig>
        extends CommonResourceRepository<T, String> {

    Optional<T> findFirstByApiKeyAndActivatedOrderByLastModifiedDateDesc(String apiKey, boolean activated);

    Optional<T> findFirstByApiKey(String apiKey);

}
