package tech.corefinance.account.crypto.service;

import tech.corefinance.account.common.service.AccountTransactionService;
import tech.corefinance.account.crypto.entity.CryptoTransaction;
import tech.corefinance.account.crypto.repository.CryptoTransactionRepository;

public interface CryptoTransactionService extends
        AccountTransactionService<CryptoTransaction, CryptoTransactionRepository> {
}
