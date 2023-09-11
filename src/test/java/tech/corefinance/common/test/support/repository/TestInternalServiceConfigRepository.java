package tech.corefinance.common.test.support.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery;
import tech.corefinance.common.repository.InternalServiceConfigRepository;
import tech.corefinance.common.test.support.model.AbstractInternalServiceConfigTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class TestInternalServiceConfigRepository implements InternalServiceConfigRepository<AbstractInternalServiceConfigTest> {
    @Override
    public Optional<AbstractInternalServiceConfigTest> findFirstByApiKeyAndActivatedOrderByLastModifiedDateDesc(String apiKey,
                                                                                                                boolean activated) {
        return Optional.empty();
    }

    @Override
    public Optional<AbstractInternalServiceConfigTest> findFirstByApiKey(String apiKey) {
        return Optional.empty();
    }

    @Override
    public <S extends AbstractInternalServiceConfigTest> S save(S entity) {
        return null;
    }

    @Override
    public <S extends AbstractInternalServiceConfigTest> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AbstractInternalServiceConfigTest> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<AbstractInternalServiceConfigTest> findAll() {
        return null;
    }

    @Override
    public List<AbstractInternalServiceConfigTest> findAllById(Iterable<String> strings) {
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
    public void delete(AbstractInternalServiceConfigTest entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends AbstractInternalServiceConfigTest> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<AbstractInternalServiceConfigTest> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<AbstractInternalServiceConfigTest> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AbstractInternalServiceConfigTest> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends AbstractInternalServiceConfigTest> Iterable<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends AbstractInternalServiceConfigTest> Iterable<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends AbstractInternalServiceConfigTest> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AbstractInternalServiceConfigTest> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AbstractInternalServiceConfigTest> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AbstractInternalServiceConfigTest, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
