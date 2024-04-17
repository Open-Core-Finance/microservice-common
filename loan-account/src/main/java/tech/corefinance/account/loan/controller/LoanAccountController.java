package tech.corefinance.account.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.account.loan.dto.CreateLoanAccountRequest;
import tech.corefinance.account.loan.entity.LoanAccount;
import tech.corefinance.account.loan.service.LoanAccountService;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;

@RestController
@RequestMapping("/loan-accounts")
@ControllerManagedResource("loanAccount")
public class LoanAccountController implements CrudController<String, LoanAccount, CreateLoanAccountRequest> {

    @Autowired
    private LoanAccountService loanAccountService;

    @Override
    public CommonService<String, LoanAccount, ?> getHandlingService() {
        return loanAccountService;
    }

}
