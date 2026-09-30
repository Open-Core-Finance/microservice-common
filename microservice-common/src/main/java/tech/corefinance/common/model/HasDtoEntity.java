package tech.corefinance.common.model;

import org.springframework.beans.BeanUtils;
import tech.corefinance.common.ex.ServiceProcessingException;

import java.io.Serializable;

public interface HasDtoEntity<E extends Serializable, T extends EntityDto<E>> extends GenericModel<E> {

    default T toDto() {
        try {
            T dto = getDtoClass().getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(this, dto);
            customDtoPropertySet(dto);
            return dto;
        } catch (ReflectiveOperationException e) {
            throw new ServiceProcessingException("Failed to instantiate dto class with default constructor!", e);
        }
    }

    Class<T> getDtoClass();

    default void customDtoPropertySet(T dto) {}
}
