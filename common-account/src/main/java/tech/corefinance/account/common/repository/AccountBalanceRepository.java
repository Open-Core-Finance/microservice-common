package tech.corefinance.account.common.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.account.common.entity.AccountBalance;
import tech.corefinance.account.common.entity.AccountBalanceId;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface AccountBalanceRepository extends CommonResourceRepository<AccountBalance, AccountBalanceId> {
}
