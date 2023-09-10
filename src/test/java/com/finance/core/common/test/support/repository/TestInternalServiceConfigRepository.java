package com.finance.core.common.test.support.repository;

import com.finance.core.common.repository.InternalServiceConfigRepository;
import com.finance.core.common.test.support.model.InternalServiceConfigTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TestInternalServiceConfigRepository implements InternalServiceConfigRepository<InternalServiceConfigTest> {
    @Override
    public Optional<InternalServiceConfigTest> findFirstByApiKeyAndActivatedOrderByLastModifiedDateDesc(String apiKey,
                                                                                                        boolean activated) {
        return Optional.empty();
    }

    @Override
    public Optional<InternalServiceConfigTest> findFirstByApiKey(String apiKey) {
        return Optional.empty();
    }

    @Override
    public <S extends InternalServiceConfigTest> S save(S entity) {
        return null;
    }

    @Override
    public <S extends InternalServiceConfigTest> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<InternalServiceConfigTest> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<InternalServiceConfigTest> findAll() {
        return null;
    }

    @Override
    public List<InternalServiceConfigTest> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(InternalServiceConfigTest entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends InternalServiceConfigTest> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<InternalServiceConfigTest> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<InternalServiceConfigTest> findAll(Pageable pageable) {
        return null;
    }
}
