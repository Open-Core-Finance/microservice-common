package tech.corefinance.account.deposit.service;

import tech.corefinance.account.common.service.AccountTransactionService;
import tech.corefinance.account.deposit.entity.DepositTransaction;
import tech.corefinance.account.deposit.repository.DepositTransactionRepository;

public interface DepositTransactionService extends
        AccountTransactionService<DepositTransaction, DepositTransactionRepository> {
}
