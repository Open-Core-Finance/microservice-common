package tech.corefinance.common.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public interface GenericModel<T extends Serializable> extends Serializable {

    @Id
    T getId();

    void setId(T id);

}
