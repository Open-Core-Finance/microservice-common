package tech.corefinance.account.loan.service;

import tech.corefinance.account.loan.entity.LoanAccount;
import tech.corefinance.account.loan.repository.LoanAccountRepository;
import tech.corefinance.common.service.CommonService;

public interface LoanAccountService extends CommonService<String, LoanAccount, LoanAccountRepository> {
    String SEQUENCE_NAME = "loan_account_id_seq";
}
