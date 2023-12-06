package tech.corefinance.common.mongodb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.corefinance.common.model.Permission;
import tech.corefinance.common.mongodb.repository.MongoPermissionRepository;
import tech.corefinance.common.service.SimpleSearchSupport;

import java.util.List;

@Service
public class PermissionSimpleSearchSupport implements SimpleSearchSupport<Permission> {

    @Autowired
    private MongoPermissionRepository mongoPermissionRepository;

    @Override
    public Page<Permission> searchByTextAndPage(Class<? extends Permission> clzz, String searchText, Pageable pageable) {
        return mongoPermissionRepository.searchBy(searchText, pageable);
    }

    @Override
    public List<Permission> searchByTextAndSort(Class<? extends Permission> clzz, String searchText, Sort sort) {
        return mongoPermissionRepository.searchBy(searchText, sort);
    }
}
