package tech.corefinance.balance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.balance.entity.AccountBalance;
import tech.corefinance.balance.entity.AccountBalanceId;
import tech.corefinance.balance.service.AccountBalanceService;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;

@RestController
@RequestMapping("/account-balances")
@ControllerManagedResource("accountBalance")
public class AccountBalanceController implements CrudController<AccountBalanceId, AccountBalance, AccountBalance> {

    @Autowired
    private AccountBalanceService accountBalanceService;

    @Override
    public CommonService<AccountBalanceId, AccountBalance, ?> getHandlingService() {
        return accountBalanceService;
    }
}
