package tech.corefinance.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

public interface CreateUpdateDto<T extends Serializable> {

    @Transient
    @jakarta.persistence.Transient
    @JsonIgnore
    default T getEntityId() {
        if (GenericModel.class.isAssignableFrom(getClass())) {
            GenericModel<T> genericModel = (GenericModel<T>) this;
            return genericModel.getId();
        }
        throw new UnsupportedOperationException("Please override getEntityId() for custom CreateUpdateDto!");
    }

    @Transient
    @jakarta.persistence.Transient
    @JsonIgnore
    default void setEntityId(T entityId) {
        if (GenericModel.class.isAssignableFrom(getClass())) {
            GenericModel<T> genericModel = (GenericModel<T>) this;
            genericModel.setId(entityId);
            return;
        }
        throw new UnsupportedOperationException("Please override setEntityId(entityId) for custom CreateUpdateDto!");
    }

}
