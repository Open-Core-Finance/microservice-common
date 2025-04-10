package tech.corefinance.common.test.support.service;

import tech.corefinance.common.entity_author.InternalServiceConfig;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.common.test.support.repository.TestInternalServiceConfigRepository;

public interface InternalServiceConfigTestService
        extends CommonService<String, InternalServiceConfig, TestInternalServiceConfigRepository> {
}
