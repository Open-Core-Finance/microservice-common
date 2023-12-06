package tech.corefinance.common.test.support.repository;

import lombok.Getter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import tech.corefinance.common.model.InternalServiceConfig;
import tech.corefinance.common.repository.InternalServiceConfigRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
@Getter
public class TestInternalServiceConfigRepository implements InternalServiceConfigRepository {

    private int saveCount;
    private List<InternalServiceConfig> savedList = new LinkedList<>();

    @Override
    public Optional<InternalServiceConfig> findFirstByApiKeyAndActivatedOrderByLastModifiedDateDesc(String apiKey,
                                                                                                    boolean activated) {
        return Optional.empty();
    }

    @Override
    public Optional<InternalServiceConfig> findFirstByApiKey(String apiKey) {
        return Optional.empty();
    }

    @Override
    public <S extends InternalServiceConfig> S save(S entity) {
        if(!savedList.contains(entity)) {
            saveCount++;
            savedList.add(entity);
        }
        return entity;
    }

    @Override
    public <S extends InternalServiceConfig> List<S> saveAll(Iterable<S> entities) {
        var result = new LinkedList<S>();
        var iterator = entities.iterator();
        while(iterator.hasNext()) {
            var entity = iterator.next();
            if(!savedList.contains(entity)) {
                saveCount++;
                savedList.add(entity);
            }
            result.add(entity);
        }
        return result;
    }

    @Override
    public Optional<InternalServiceConfig> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<InternalServiceConfig> findAll() {
        return new LinkedList<>();
    }

    @Override
    public List<InternalServiceConfig> findAllById(Iterable<String> strings) {
        return new LinkedList<>();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(InternalServiceConfig entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends InternalServiceConfig> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<InternalServiceConfig> findAll(Sort sort) {
        return new LinkedList<>();
    }

    @Override
    public Page<InternalServiceConfig> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends InternalServiceConfig> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends InternalServiceConfig> Iterable<S> findAll(Example<S> example) {
        return new LinkedList<>();
    }

    @Override
    public <S extends InternalServiceConfig> Iterable<S> findAll(Example<S> example, Sort sort) {
        return new LinkedList<>();
    }

    @Override
    public <S extends InternalServiceConfig> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends InternalServiceConfig> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends InternalServiceConfig> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends InternalServiceConfig, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
