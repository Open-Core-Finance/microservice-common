package tech.corefinance.common.mongodb.model;

import tech.corefinance.common.model.GenericModel;

public interface IdSequentialModel extends GenericModel<Long> {

    default String getIdSequenceName() {
        return getClass().getSimpleName();
    }

}
