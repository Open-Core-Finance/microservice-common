package tech.corefinance.common.test.support.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import tech.corefinance.common.entity_author.AnonymousUrlAccess;
import tech.corefinance.common.repository.AnonymousUrlAccessRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class TestAnonymousUrlAccessRepository implements AnonymousUrlAccessRepository {


    @Override
    public <S extends AnonymousUrlAccess> S save(S entity) {
        return null;
    }

    @Override
    public <S extends AnonymousUrlAccess> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AnonymousUrlAccess> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<AnonymousUrlAccess> findAll() {
        return new LinkedList<>();
    }

    @Override
    public List<AnonymousUrlAccess> findAllById(Iterable<String> strings) {
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
    public void delete(AnonymousUrlAccess entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends AnonymousUrlAccess> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<AnonymousUrlAccess> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<AnonymousUrlAccess> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AnonymousUrlAccess> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends AnonymousUrlAccess> Iterable<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends AnonymousUrlAccess> Iterable<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends AnonymousUrlAccess> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AnonymousUrlAccess> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AnonymousUrlAccess> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AnonymousUrlAccess, R> R findBy(Example<S> example,
                                                      Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
