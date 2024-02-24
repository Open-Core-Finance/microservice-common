package tech.corefinance.account.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.account.deposit.dto.DepositTransactionRequest;
import tech.corefinance.account.deposit.entity.DepositTransaction;
import tech.corefinance.account.deposit.service.DepositTransactionService;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;

@RestController
@RequestMapping("/deposit-transactions")
@ControllerManagedResource("deposittransaction")
public class DepositTransactionController implements CrudController<String, DepositTransaction, DepositTransactionRequest> {

    @Autowired
    private DepositTransactionService depositTransactionService;

    @Override
    public CommonService<String, DepositTransaction, ?> getHandlingService() {
        return depositTransactionService;
    }
}
