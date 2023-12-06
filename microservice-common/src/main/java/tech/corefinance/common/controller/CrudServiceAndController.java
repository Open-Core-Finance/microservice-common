package tech.corefinance.common.controller;

import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.common.service.CommonService;

import java.io.Serializable;

public interface CrudServiceAndController<I extends Serializable, T extends GenericModel<I>, D extends CreateUpdateDto<I>,
        R extends CommonResourceRepository<T, I>> extends CrudController<I, T, D>, CommonService<I, T, R> {
    @Override
    default CommonService<I, T, ?> getHandlingService() {
        return this;
    }
}
