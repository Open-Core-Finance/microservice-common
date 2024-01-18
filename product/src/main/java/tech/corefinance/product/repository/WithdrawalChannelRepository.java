package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.WithdrawalChannel;

@Repository
public interface WithdrawalChannelRepository extends JpaRepository<WithdrawalChannel, String>,
        CommonResourceRepository<WithdrawalChannel, String> {
}
