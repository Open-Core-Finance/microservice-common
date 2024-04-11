package tech.corefinance.account.crypto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.account.crypto.entity.CryptoAccount;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface CryptoAccountRepository extends CommonResourceRepository<CryptoAccount, String>,
        JpaRepository<CryptoAccount, String> {
}
