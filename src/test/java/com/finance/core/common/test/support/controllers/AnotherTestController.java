package com.finance.core.common.test.support.controllers;

import com.finance.core.common.model.ResourceAction;
import com.finance.core.common.test.support.model.UserWrapterTest;
import com.finance.core.common.annotation.ControllerManagedResource;
import com.finance.core.common.annotation.ManualPermissionCheck;
import com.finance.core.common.annotation.PermissionAction;
import com.finance.core.common.annotation.PermissionResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@ControllerManagedResource("test-common")
public class AnotherTestController {

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

    @PostMapping("/request-with-complex-resource")
    @PermissionAction(action = ResourceAction.COMMON_ACTION_LIST)
    public void complexResource(HttpServletResponse response,
                                @RequestBody
                                @PermissionResource(resourceType = "custom-resource-type", idPath = "user.id")
                                UserWrapterTest user) throws IOException {
        writeTest(response);
    }
}
