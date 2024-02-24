package tech.corefinance.account.deposit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.account.common.service.AccountTransactionServiceImpl;
import tech.corefinance.account.deposit.entity.DepositTransaction;
import tech.corefinance.account.deposit.repository.DepositTransactionRepository;

@Service
@Transactional
public class DepositTransactionServiceImpl
        extends AccountTransactionServiceImpl<DepositTransaction, DepositTransactionRepository>
        implements DepositTransactionService {

    @Autowired
    private DepositTransactionRepository depositTransactionRepository;

    @Override
    public DepositTransactionRepository getRepository() {
        return depositTransactionRepository;
    }
}
