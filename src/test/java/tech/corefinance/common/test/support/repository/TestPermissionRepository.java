package tech.corefinance.common.test.support.repository;

import tech.corefinance.common.repository.PermissionRepository;
import tech.corefinance.common.test.support.model.PermissionTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

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
        return null;
    }

    @Override
    public Page<PermissionTest> searchBy(String searchText, Pageable pageRequest) {
        return null;
    }

    @Override
    public <S extends PermissionTest> S save(S entity) {
        return null;
    }

    @Override
    public <S extends PermissionTest> List<S> saveAll(Iterable<S> entities) {
        return null;
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
        return null;
    }

    @Override
    public List<PermissionTest> findAllById(Iterable<String> strings) {
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
        return null;
    }

    @Override
    public Page<PermissionTest> findAll(Pageable pageable) {
        return null;
    }
}
