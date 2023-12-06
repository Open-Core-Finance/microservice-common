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

    @PostMapping(value = "/create-or-update")
    default GeneralApiResponse<?> createOrUpdate(@RequestBody D role) {
        var converter = getEntityConverter();
        var result = getHandlingService().createOrUpdateEntity(role);
        if (converter == null) {
            return new GeneralApiResponse<>(result);
        }
        return new GeneralApiResponse<>(converter.convert(result));
    }

    @DeleteMapping(value = "/delete")
    default GeneralApiResponse<Boolean> delete(@RequestParam("entityId") I entityId) {
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
