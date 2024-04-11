package tech.corefinance.account.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.account.deposit.dto.CreateDepositAccountRequest;
import tech.corefinance.account.deposit.entity.DepositAccount;
import tech.corefinance.account.deposit.service.DepositAccountService;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;

@RestController
@RequestMapping("/deposit-accounts")
@ControllerManagedResource("depositAccount")
public class DepositAccountController implements CrudController<String, DepositAccount, CreateDepositAccountRequest> {

    @Autowired
    private DepositAccountService depositAccountService;

    @Override
    public CommonService<String, DepositAccount, ?> getHandlingService() {
        return depositAccountService;
    }
}
