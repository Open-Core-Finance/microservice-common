package tech.corefinance.account.loan.service;

import tech.corefinance.account.common.service.AccountTransactionService;
import tech.corefinance.account.loan.entity.LoanTransaction;
import tech.corefinance.account.loan.repository.LoanTransactionRepository;

public interface LoanTransactionService extends
        AccountTransactionService<LoanTransaction, LoanTransactionRepository> {
}
