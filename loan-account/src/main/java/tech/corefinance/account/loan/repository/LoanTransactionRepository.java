package tech.corefinance.account.loan.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.account.loan.entity.LoanTransaction;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface LoanTransactionRepository extends CommonResourceRepository<LoanTransaction, String> {
}
