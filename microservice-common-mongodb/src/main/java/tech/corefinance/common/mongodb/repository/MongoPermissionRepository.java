package tech.corefinance.common.mongodb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.model.Permission;

import java.util.List;

@Repository
public interface MongoPermissionRepository extends MongoRepository<Permission, String> {

    String searchByQuery = "{$or: [" + "{'roleId': { $regex: :#{#search}, $options: 'i' }}"
            + ",{'resourceType': { $regex: :#{#search}, $options: 'i' }}" + ",{'action': { $regex: :#{#search}, $options: 'i' }}"
            + ",{'url': { $regex: :#{#search}, $options: 'i' }}" + ",{'control': { $regex: :#{#search}, $options: 'i' }}"
            + ",{'requestMethod': { $regex: :#{#search}, $options: 'i' }}" + "]}";

    @Query(searchByQuery)
    Page<Permission> searchBy(@Param("search") String searchText, Pageable pageable);

    @Query(searchByQuery)
    List<Permission> searchBy(@Param("search") String searchText, Sort sort);
}
