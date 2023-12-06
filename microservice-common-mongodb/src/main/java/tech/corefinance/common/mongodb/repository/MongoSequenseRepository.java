package tech.corefinance.common.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.mongodb.model.MongoSequence;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface MongoSequenseRepository extends MongoRepository<MongoSequence, String>, CommonResourceRepository<MongoSequence, String> {

}
