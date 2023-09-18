package tech.corefinance.common.test.support.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery;
import tech.corefinance.common.repository.PermissionRepository;
import tech.corefinance.common.test.support.model.PermissionTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class TestPermissionRepository
        implements PermissionRepository<PermissionTest> {
    @Override
    public Optional<PermissionTest> findFirstByRoleIdAndResourceTypeAndActionAndUrlAndRequestMethod(String roleId,
                                                                                                    String resourceType,
                                                                                                    String action,
                                                                                                    String url,
                                                                                                    RequestMethod requestMethod) {
        return Optional.empty();
    }

    @Override
    public List<PermissionTest> findAllByRoleIdAndResourceType(String roleId, String resourceType, Sort sort) {
        return new LinkedList<>();
    }

    @Override
    public PermissionTest save(PermissionTest entity) {
        return entity;
    }

    @Override
    public <S extends PermissionTest> List<S> saveAll(Iterable<S> entities) {
        var result = new LinkedList<S>();
        var iterator = entities.iterator();
        while(iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    @Override
    public Optional<PermissionTest> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<PermissionTest> findAll() {
        return new LinkedList<>();
    }

    @Override
    public List<PermissionTest> findAllById(Iterable<String> strings) {
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
    public void delete(PermissionTest entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends PermissionTest> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<PermissionTest> findAll(Sort sort) {
        return new LinkedList<>();
    }

    @Override
    public Page<PermissionTest> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PermissionTest> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PermissionTest> Iterable<S> findAll(Example<S> example) {
        return new LinkedList<>();
    }

    @Override
    public <S extends PermissionTest> Iterable<S> findAll(Example<S> example, Sort sort) {
        return new LinkedList<>();
    }

    @Override
    public <S extends PermissionTest> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PermissionTest> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PermissionTest> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends PermissionTest, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
