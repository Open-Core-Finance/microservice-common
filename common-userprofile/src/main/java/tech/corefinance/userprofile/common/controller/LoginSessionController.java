package tech.corefinance.userprofile.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.userprofile.common.entity_author.CommonLoginSession;
import tech.corefinance.userprofile.common.repository.CommonLoginSessionRepository;

@RestController
@RequestMapping("/login-sessions")
@ControllerManagedResource("loginsession")
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public class LoginSessionController implements
        CrudServiceAndController<String, CommonLoginSession<?>, CommonLoginSession<?>, CommonLoginSessionRepository<CommonLoginSession<?>>> {

    @SuppressWarnings("rawtypes")
    @Autowired
    private CommonLoginSessionRepository commonLoginSessionRepository;

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public CommonLoginSessionRepository getRepository() {
        return commonLoginSessionRepository;
    }
}
