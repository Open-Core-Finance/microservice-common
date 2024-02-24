package tech.corefinance.account.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.account.common.service.AccountTransactionServiceImpl;
import tech.corefinance.account.loan.entity.LoanTransaction;
import tech.corefinance.account.loan.repository.LoanTransactionRepository;

@Service
@Transactional
public class LoanTransactionServiceImpl
        extends AccountTransactionServiceImpl<LoanTransaction, LoanTransactionRepository>
        implements LoanTransactionService {

    @Autowired
    private LoanTransactionRepository loanTransactionRepository;

    @Override
    public LoanTransactionRepository getRepository() {
        return loanTransactionRepository;
    }
}
