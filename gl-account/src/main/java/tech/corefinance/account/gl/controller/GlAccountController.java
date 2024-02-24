package tech.corefinance.account.gl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.account.gl.entity.GlAccount;
import tech.corefinance.account.gl.repository.GlAccountRepository;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;

@RestController
@RequestMapping("/gl-accounts")
@ControllerManagedResource("glaccount")
public class GlAccountController
        implements CrudServiceAndController<String, GlAccount, GlAccount, GlAccountRepository> {
    @Autowired
    private GlAccountRepository glAccountRepository;

    @Override
    public GlAccountRepository getRepository() {
        return glAccountRepository;
    }
}
