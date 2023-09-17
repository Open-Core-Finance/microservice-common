package tech.corefinance.common.test.support.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import tech.corefinance.common.repository.ResourceActionRepository;
import tech.corefinance.common.test.support.model.ResourceActionTest;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class TestResourceActionRepository
        implements ResourceActionRepository<ResourceActionTest> {

    @Override
    public ResourceActionTest save(ResourceActionTest entity) {
        return entity;
    }

    @Override
    public <S extends ResourceActionTest> List<S> saveAll(Iterable<S> entities) {
        var result = new LinkedList<S>();
        var iterator = entities.iterator();
        while(iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    @Override
    public Optional<ResourceActionTest> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<ResourceActionTest> findAll() {
        return new LinkedList<>();
    }

    @Override
    public List<ResourceActionTest> findAllById(Iterable<String> strings) {
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
    public void delete(ResourceActionTest entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ResourceActionTest> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ResourceActionTest> findAll(Sort sort) {
        return new LinkedList<>();
    }

    @Override
    public Page<ResourceActionTest> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ResourceActionTest> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ResourceActionTest> Iterable<S> findAll(Example<S> example) {
        return new LinkedList<>();
    }

    @Override
    public <S extends ResourceActionTest> Iterable<S> findAll(Example<S> example, Sort sort) {
        return new LinkedList<>();
    }

    @Override
    public <S extends ResourceActionTest> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ResourceActionTest> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ResourceActionTest> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ResourceActionTest, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
