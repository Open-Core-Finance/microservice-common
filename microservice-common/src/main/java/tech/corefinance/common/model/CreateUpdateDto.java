package tech.corefinance.common.model;

import java.io.Serializable;

public interface CreateUpdateDto<T extends Serializable> extends GenericModel<T> {
    default boolean supportAutoValidation() {return false;}
}
