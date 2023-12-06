package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String>,
        CommonResourceRepository<Currency, String> {
}
