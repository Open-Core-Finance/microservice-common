package tech.corefinance.common.controller;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.dto.PageDto;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.service.CommonService;

import java.io.Serializable;
import java.util.List;

public interface CrudController<I extends Serializable ,T extends GenericModel<I>, D extends CreateUpdateDto<I>> {

    CommonService<I, T, ?> getHandlingService();

    @PermissionAction(action = AbstractResourceAction.COMMON_ACTION_LIST)
    @PostMapping(value = "/")
    default PageDto<T> search(@RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "orders", required = false, defaultValue = "[]")
                                   List<Sort.Order> orders,
                                   @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText) {
        return PageDto.createSuccessResponse(getHandlingService().searchData(searchText, pageSize, pageIndex, orders));
    }

    @PostMapping(value = "/create-or-update")
    default GeneralApiResponse<T> createOrUpdateRole(@RequestBody D role) {
        return new GeneralApiResponse<>(getHandlingService().createOrUpdateEntity(role));
    }

    @DeleteMapping(value = "/delete")
    default GeneralApiResponse<Boolean> deleteRole(@RequestParam("entityId") I entityId) {
        return new GeneralApiResponse<>(getHandlingService().deleteEntity(entityId));
    }

}
