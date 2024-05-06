package tech.corefinance.balance.service;

import tech.corefinance.balance.entity.AccountBalance;
import tech.corefinance.balance.entity.AccountBalanceId;
import tech.corefinance.balance.repository.AccountBalanceRepository;
import tech.corefinance.common.service.CommonService;

public interface AccountBalanceService extends CommonService<AccountBalanceId, AccountBalance, AccountBalanceRepository> {
}
