package tech.corefinance.account.deposit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.account.deposit.entity.DepositTransaction;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface DepositTransactionRepository extends CommonResourceRepository<DepositTransaction, String>,
        JpaRepository<DepositTransaction, String> {
}
