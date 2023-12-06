package tech.corefinance.common.mongodb.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import tech.corefinance.common.mongodb.model.IdSequentialModel;
import tech.corefinance.common.mongodb.repository.MongoSequenceCustomRepository;

@Component
public class IdSequentialModelListener extends AbstractMongoEventListener<IdSequentialModel> {

    @Autowired
    private MongoSequenceCustomRepository mongoSequenceCustomRepository;

    @Autowired
    private AuditingHandler auditingHandler;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<IdSequentialModel> event) {
        super.onBeforeConvert(event);
        var source = event.getSource();
        if (source.getId() == null || source.getId() < 1) {
            source.setId(mongoSequenceCustomRepository.nextSequence(source.getIdSequenceName()));
            auditingHandler.markCreated(source);
        }
    }
}
