package tech.corefinance.userprofile.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.userprofile.common.entity_author.AttemptedLogin;
import tech.corefinance.userprofile.common.repository.AttemptedLoginRepository;

@RestController
@RequestMapping("/attempted-logins")
@ControllerManagedResource("attemptedlogin")
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public class AttemptedLoginController
        implements CrudServiceAndController<String, AttemptedLogin, AttemptedLogin, AttemptedLoginRepository> {

    @Autowired
    private AttemptedLoginRepository attemptedLoginRepository;

    @Override
    public AttemptedLoginRepository getRepository() {
        return attemptedLoginRepository;
    }
}
