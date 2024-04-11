package tech.corefinance.account.deposit.service;

import tech.corefinance.account.common.service.AccountService;
import tech.corefinance.account.deposit.entity.DepositAccount;
import tech.corefinance.account.deposit.repository.DepositAccountRepository;

public interface DepositAccountService extends AccountService<DepositAccount, DepositAccountRepository> {

    String SEQUENCE_NAME = "deposit_account_id_seq";

}
