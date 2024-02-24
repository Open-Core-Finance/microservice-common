package tech.corefinance.account.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.account.loan.dto.LoanTransactionRequest;
import tech.corefinance.account.loan.entity.LoanTransaction;
import tech.corefinance.account.loan.service.LoanTransactionService;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;

@RestController
@RequestMapping("/loan-transactions")
@ControllerManagedResource("loantransaction")
public class LoanTransactionController implements CrudController<String, LoanTransaction, LoanTransactionRequest> {

    @Autowired
    private LoanTransactionService loanTransactionService;

    @Override
    public CommonService<String, LoanTransaction, ?> getHandlingService() {
        return loanTransactionService;
    }
}
