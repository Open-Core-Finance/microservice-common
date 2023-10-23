package tech.corefinance.common.model;

import org.springframework.data.annotation.Transient;

import java.io.Serializable;

public interface CreateUpdateDto<T extends Serializable> {

    @Transient
    @jakarta.persistence.Transient
    default T getEntityId() {
        if (GenericModel.class.isAssignableFrom(getClass())) {
            GenericModel<T> genericModel = (GenericModel<T>) this;
            return genericModel.getId();
        }
        throw new UnsupportedOperationException("Please override getEntityId() for custom CreateUpdateDto!");
    }

}
