package tech.corefinance.common.test.support.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.entity_author.Permission;
import tech.corefinance.common.repository.PermissionRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings({"unchecked"})
@Component
public class TestPermissionRepository implements PermissionRepository {
    @Override
    public Optional<Permission> findFirstByRoleIdAndResourceTypeAndActionAndUrlAndRequestMethod(String roleId,
                                                                                                String resourceType,
                                                                                                String action,
                                                                                                String url,
                                                                                                RequestMethod requestMethod) {
        return Optional.empty();
    }

    @Override
    public List<Permission> findAllByRoleIdAndResourceType(String roleId, String resourceType, Sort sort) {
        return new LinkedList<>();
    }

    @Override
    public List<Permission> findByRoleIdIn(Iterable<String> roleIds) {
        return new LinkedList<>();
    }

    @Override
    public void deleteAllByRoleIdIn(Iterable<? extends String> roleIds) {

    }

    @Override
    public Permission save(Permission entity) {
        return entity;
    }

    @Override
    public <S extends Permission> List<S> saveAll(Iterable<S> entities) {
        var result = new LinkedList<S>();
        var iterator = entities.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    @Override
    public Optional<Permission> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Permission> findAll() {
        return new LinkedList<>();
    }

    @Override
    public List<Permission> findAllById(Iterable<String> strings) {
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
    public void delete(Permission entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Permission> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Permission> findAll(Sort sort) {
        return new LinkedList<>();
    }

    @Override
    public Page<Permission> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Permission> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Permission> Iterable<S> findAll(Example<S> example) {
        return new LinkedList<>();
    }

    @Override
    public <S extends Permission> Iterable<S> findAll(Example<S> example, Sort sort) {
        return new LinkedList<>();
    }

    @Override
    public <S extends Permission> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Permission> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Permission> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Permission, R> R findBy(Example<S> example,
                                              Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
