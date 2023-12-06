package tech.corefinance.userprofile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.userprofile.entity.AttemptedLogin;
import tech.corefinance.userprofile.entity.LoginSession;
import tech.corefinance.userprofile.repository.AttemptedLoginRepository;
import tech.corefinance.userprofile.repository.LoginSessionRepository;

@RestController
@RequestMapping("/login-sessions")
@ControllerManagedResource("loginsession")
public class LoginSessionController
        implements CrudServiceAndController<String, LoginSession, LoginSession, LoginSessionRepository> {

    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @Override
    public LoginSessionRepository getRepository() {
        return loginSessionRepository;
    }
}
