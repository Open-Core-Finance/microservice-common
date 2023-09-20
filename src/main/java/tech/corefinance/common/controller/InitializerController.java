package tech.corefinance.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.model.AbstractResourceAction;
import tech.corefinance.common.service.InitialSupportService;

import java.io.IOException;
import java.util.Map;

public interface InitializerController {

    InitialSupportService getInitialSupportService();

    @PermissionAction(action = AbstractResourceAction.COMMON_ACTION_INITIAL)
    @GetMapping(value = "/initialization-default-data")
    default GeneralApiResponse<Map<String, Object>> initializationDefaultData() throws IOException {
        return new GeneralApiResponse<>(getInitialSupportService().initializationDefaultData());
    }

}
