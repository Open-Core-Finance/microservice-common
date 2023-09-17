package tech.corefinance.common.model;

import java.io.Serializable;

public interface CreateUpdateDto<T extends Serializable> {

    T getEntityId();

}
