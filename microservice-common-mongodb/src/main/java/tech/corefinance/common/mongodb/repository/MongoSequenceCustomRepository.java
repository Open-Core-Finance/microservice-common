package tech.corefinance.common.mongodb.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.mongodb.model.MongoSequence;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@Slf4j
public class MongoSequenceCustomRepository {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private MongoSequenseRepository mongoSequenseRepository;

    private static Object lock = new Object();

    public long nextSequence(String seqName) {
        MongoSequence mongoSequence = getDbSequence(seqName);
        if (Objects.isNull(mongoSequence) || Objects.isNull(mongoSequence.sequence())) {
            log.info("Creating new sequence for [{}]", seqName);
            synchronized (lock) {
                // Double DB query for multiple thread processing
                mongoSequence = getDbSequence(seqName);
                if (Objects.isNull(mongoSequence) || Objects.isNull(mongoSequence.sequence())) {
                    mongoSequence = new MongoSequence(seqName, 0L);
                    mongoSequenseRepository.save(mongoSequence);
                }
            }
        } else {
            log.info("R sequence for [{}]", seqName);
        }
        return mongoSequence.sequence();
    }

    private MongoSequence getDbSequence(String seqName) {
        return mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("sequence", 1), options().returnNew(false).upsert(true),
                MongoSequence.class);
    }
}
