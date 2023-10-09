package tech.corefinance.common.test.support.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.corefinance.common.test.support.repository.TestInternalServiceConfigRepository;

@Service
public class InternalServiceConfigTestServiceImpl implements InternalServiceConfigTestService {

    @Autowired
    private TestInternalServiceConfigRepository repository;

    @Override
    public TestInternalServiceConfigRepository getRepository() {
        return repository;
    }
}
