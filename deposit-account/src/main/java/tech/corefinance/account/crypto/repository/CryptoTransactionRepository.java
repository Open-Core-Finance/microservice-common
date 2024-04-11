package tech.corefinance.account.crypto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.account.crypto.entity.CryptoTransaction;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface CryptoTransactionRepository extends CommonResourceRepository<CryptoTransaction, String>,
        JpaRepository<CryptoTransaction, String> {
}
