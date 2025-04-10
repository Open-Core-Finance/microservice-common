package tech.corefinance.userprofile.controllers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;

@RestController
@ConditionalOnProperty(prefix = "tech.corefinance.app.userprofile", name = "enable-index-controller", havingValue = "true")
@ControllerManagedResource("userprofileindex")
public class IndexController {

    @GetMapping({"/", "/index"})
    public String index() {
        return "{\"status\": \"OK\"}";
    }
}
