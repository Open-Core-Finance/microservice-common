package tech.corefinance.account.loan.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.account.loan.entity.LoanAccount;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface LoanAccountRepository extends CommonResourceRepository<LoanAccount, String> {
}
