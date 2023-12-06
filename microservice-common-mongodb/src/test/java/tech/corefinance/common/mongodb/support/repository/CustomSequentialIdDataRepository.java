package tech.corefinance.common.mongodb.support.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.mongodb.support.model.CustomSequentialIdData;

@Repository
public interface CustomSequentialIdDataRepository extends MongoRepository<CustomSequentialIdData, Long> {
}
