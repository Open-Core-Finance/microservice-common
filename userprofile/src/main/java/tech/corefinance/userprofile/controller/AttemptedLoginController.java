package tech.corefinance.userprofile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.userprofile.entity.AttemptedLogin;
import tech.corefinance.userprofile.repository.AttemptedLoginRepository;

@RestController
@RequestMapping("/attempted-logins")
@ControllerManagedResource("attemptedlogin")
public class AttemptedLoginController
        implements CrudServiceAndController<String, AttemptedLogin, AttemptedLogin, AttemptedLoginRepository> {

    @Autowired
    private AttemptedLoginRepository attemptedLoginRepository;

    @Override
    public AttemptedLoginRepository getRepository() {
        return attemptedLoginRepository;
    }
}
