package tech.corefinance.common.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Document which store all sequence.
 * @param id Sequence ID
 * @param sequence sequence value
 */
@Document(collection = "mongo_sequense")
public record MongoSequence(@Id String id, Long sequence) {
    public MongoSequence() {
        this(UUID.randomUUID().toString(), 0L);
    }
}