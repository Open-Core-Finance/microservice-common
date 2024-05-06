package tech.corefinance.balance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import tech.corefinance.account.common.model.AccountType;
import tech.corefinance.balance.entity.AccountBalance;
import tech.corefinance.balance.entity.AccountBalanceId;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface AccountBalanceRepository extends CommonResourceRepository<AccountBalance, AccountBalanceId>,
        JpaRepository<AccountBalance, AccountBalanceId> {

    @Modifying
    void deleteAllByIdAccountTypeAndIdAccountId(AccountType accountType, String accountId);
}
