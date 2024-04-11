package tech.corefinance.account.crypto.service;

import tech.corefinance.account.common.service.AccountService;
import tech.corefinance.account.crypto.entity.CryptoAccount;
import tech.corefinance.account.crypto.repository.CryptoAccountRepository;

public interface CryptoAccountService extends AccountService<CryptoAccount, CryptoAccountRepository> {

    String SEQUENCE_NAME = "crypto_account_id_seq";

}
