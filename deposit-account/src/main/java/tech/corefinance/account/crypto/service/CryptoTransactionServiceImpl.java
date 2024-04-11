package tech.corefinance.account.crypto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.account.common.service.AccountTransactionServiceImpl;
import tech.corefinance.account.crypto.entity.CryptoTransaction;
import tech.corefinance.account.crypto.repository.CryptoTransactionRepository;

@Service
@Transactional
public class CryptoTransactionServiceImpl extends AccountTransactionServiceImpl<CryptoTransaction, CryptoTransactionRepository>
        implements CryptoTransactionService {

    @Autowired
    private CryptoTransactionRepository cryptoTransactionRepository;

    @Override
    public CryptoTransactionRepository getRepository() {
        return cryptoTransactionRepository;
    }
}
