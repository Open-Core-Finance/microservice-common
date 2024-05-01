package tech.corefinance.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.payment.entity.InternalFundTransfer;

@Repository
public interface InternalFundTransferRepository extends CommonResourceRepository<InternalFundTransfer, String>,
        JpaRepository<InternalFundTransfer, String> {
}
