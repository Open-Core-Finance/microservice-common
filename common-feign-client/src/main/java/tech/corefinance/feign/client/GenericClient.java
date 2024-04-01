package tech.corefinance.feign.client;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.dto.PageDto;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;

import java.io.Serializable;
import java.util.List;

public interface GenericClient<I extends Serializable, T extends GenericModel<I>, D extends CreateUpdateDto<I>> {

    @PostMapping(value = "/")
    PageDto<T> search(@RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                              @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                              @RequestParam(value = "orders", required = false, defaultValue = "[]")
                              List<Sort.Order> orders,
                              @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText);

    @PostMapping(value = "/create")
    GeneralApiResponse<T> createEntity(@RequestBody D entity);

    @PutMapping(value = "/{entityId}")
    GeneralApiResponse<T> updateEntity(@PathVariable("entityId") I entityId, @RequestBody D entity);

    @DeleteMapping(value = "/{entityId}")
    GeneralApiResponse<Boolean> delete(@PathVariable("entityId") I entityId);

    @GetMapping(value = "/{entityId}")
    GeneralApiResponse<T> viewDetails(@PathVariable("entityId") I entityId);
}
