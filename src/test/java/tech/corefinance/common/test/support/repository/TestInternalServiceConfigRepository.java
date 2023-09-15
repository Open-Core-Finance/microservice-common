package tech.corefinance.common.test.support.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery;
import tech.corefinance.common.repository.InternalServiceConfigRepository;
import tech.corefinance.common.test.support.model.InternalServiceConfigTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

    @Override
    public <S extends InternalServiceConfigTest> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends InternalServiceConfigTest> Iterable<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends InternalServiceConfigTest> Iterable<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends InternalServiceConfigTest> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends InternalServiceConfigTest> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends InternalServiceConfigTest> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends InternalServiceConfigTest, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
