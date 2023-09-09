package com.finance.core.common.services;

import com.finance.core.common.model.GenericModel;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.Optional;

public interface CommonService<I extends Serializable, T extends GenericModel<I>, R extends CrudRepository<T, I>> {

    R getRepository();

    default boolean deleteRecord(I itemId) {
        // TODO how check delete permission
        R repository = getRepository();
        Optional<T> optional = repository.findById(itemId);
        if (optional.isPresent()) {
            T item = optional.get();
            repository.delete(item);
            return true;
        } else {
            return false;
        }
    }

}
