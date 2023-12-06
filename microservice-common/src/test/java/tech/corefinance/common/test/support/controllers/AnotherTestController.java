package tech.corefinance.common.test.support.controllers;

import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.common.test.support.model.UserWrapterTest;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.annotation.ManualPermissionCheck;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.annotation.PermissionResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@ControllerManagedResource("test-common")
public class AnotherTestController implements CrudController {

    private void writeTest(HttpServletResponse response) throws IOException {
        try (var writer = response.getWriter()) {
            writer.write("Test");
        }
    }

    @PostMapping("/another-normal")
    @ManualPermissionCheck
    public void anotherNormal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        writeTest(response);
    }

    @PostMapping("/another-normal-2")
    @PermissionAction(action = ResourceAction.COMMON_ACTION_VIEW)
    public void anotherNormal2(
            @RequestParam("resourceId") @PermissionResource(resourceType = "custom-resource-type") String resourceId,
            HttpServletResponse response) throws IOException {
        writeTest(response);
    }

    @PostMapping("/another-normal-3")
    @PermissionAction(action = ResourceAction.COMMON_ACTION_VIEW)
    public void anotherNormal3(HttpServletResponse response) throws IOException {
        writeTest(response);
    }

    @PostMapping("/request-with-complex-resource")
    @PermissionAction(action = ResourceAction.COMMON_ACTION_LIST)
    public void complexResource(HttpServletResponse response,
                                @RequestBody
                                @PermissionResource(resourceType = "custom-resource-type", idPath = "user.id")
                                UserWrapterTest user) throws IOException {
        writeTest(response);
    }

    @Override
    public CommonService getHandlingService() {
        return null;
    }
}
