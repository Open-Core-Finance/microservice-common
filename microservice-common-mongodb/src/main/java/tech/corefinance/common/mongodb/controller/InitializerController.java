package tech.corefinance.common.mongodb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.dto.GeneralApiResponse;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.mongodb.service.InitialSupportService;

import java.io.IOException;
import java.util.Map;

public interface InitializerController {

    InitialSupportService getInitialSupportService();

    @PermissionAction(action = ResourceAction.COMMON_ACTION_INITIAL)
    @GetMapping(value = "/initialization-default-data")
    default GeneralApiResponse<Map<String, Object>> initializationDefaultData() throws IOException {
        return new GeneralApiResponse<>(getInitialSupportService().initializationDefaultData());
    }

}
