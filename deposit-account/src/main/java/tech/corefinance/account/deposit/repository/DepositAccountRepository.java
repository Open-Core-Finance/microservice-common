package tech.corefinance.account.deposit.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.account.deposit.entity.DepositAccount;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface DepositAccountRepository extends CommonResourceRepository<DepositAccount, String> {
}
