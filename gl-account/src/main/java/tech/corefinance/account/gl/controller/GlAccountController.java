package tech.corefinance.account.gl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.account.gl.dto.CreateGlAccountRequest;
import tech.corefinance.account.gl.entity.GlAccount;
import tech.corefinance.account.gl.service.GlAccountService;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;

@RestController
@RequestMapping("/gl-accounts")
@ControllerManagedResource("glaccount")
public class GlAccountController implements CrudController<String, GlAccount, CreateGlAccountRequest> {

    @Autowired
    private GlAccountService glAccountService;

    @Override
    public CommonService<String, GlAccount, ?> getHandlingService() {
        return glAccountService;
    }
}
