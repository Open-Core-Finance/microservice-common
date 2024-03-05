package tech.corefinance.common.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.dto.PageDto;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.service.CommonService;

import java.io.Serializable;
import java.util.List;

public interface CrudController<I extends Serializable ,T extends GenericModel<I>, D extends CreateUpdateDto<I>> {

    CommonService<I, T, ?> getHandlingService();

    default Converter<T, ?> getEntityConverter() {
        return null;
    }

    @PermissionAction(action = ResourceAction.COMMON_ACTION_LIST)
    @PostMapping(value = "/")
    default PageDto<?> search(@RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "orders", required = false, defaultValue = "[]")
                                   List<Sort.Order> orders,
                                   @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText) {
        var converter = getEntityConverter();
        var result = getHandlingService().searchData(searchText, pageSize, pageIndex, orders);
        if (converter == null) {
            return PageDto.createSuccessResponse(result);
        }
        return PageDto.createSuccessResponse(result.map(converter::convert));
    }

    /**
     * Create new entity
     * @param entity Entity Object
     * @return Create result.
     */
    @PostMapping(value = "/create")
    default GeneralApiResponse<?> createEntity(@RequestBody D entity) {
        entity.setId(null);
        return createOrUpdate(entity);
    }

    default GeneralApiResponse<?> createOrUpdate(@RequestBody D entity) {
        var converter = getEntityConverter();
        var result = getHandlingService().createOrUpdateEntity(entity);
        if (converter == null) {
            return new GeneralApiResponse<>(result);
        }
        return new GeneralApiResponse<>(converter.convert(result));
    }

    /**
     * Update entity
     * @param entityId Entity ID
     * @param entity Entity Object
     * @return Updated result.
     */
    @PutMapping(value = "/{entityId}")
    default GeneralApiResponse<?> updateEntity(@PathVariable("entityId") I entityId, @RequestBody D entity) {
        entity.setId(entityId);
        return createOrUpdate(entity);
    }

    /**
     * Delete entity by ID.
     * @param entityId Entity ID
     * @return True if deleted
     */
    @DeleteMapping(value = "/{entityId}")
    default GeneralApiResponse<Boolean> delete(@PathVariable("entityId") I entityId) {
        return new GeneralApiResponse<>(getHandlingService().deleteEntity(entityId));
    }

    @GetMapping(value = "/{entityId}")
    default GeneralApiResponse<Object> viewDetails(@PathVariable("entityId") I entityId) {
        var converter = getEntityConverter();
        var result = getHandlingService().getEntityDetails(entityId);
        if (converter == null) {
            return new GeneralApiResponse<>(result);
        }
        return new GeneralApiResponse<>(converter.convert(result));
    }
}
