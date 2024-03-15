package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.ExchangeRate;
import tech.corefinance.product.entity.ExchangeRateId;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, ExchangeRateId>,
        CommonResourceRepository<ExchangeRate, ExchangeRateId> {
}
