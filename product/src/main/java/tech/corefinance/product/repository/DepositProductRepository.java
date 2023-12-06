package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.DepositProduct;

@Repository
public interface DepositProductRepository extends JpaRepository<DepositProduct, String>,
        CommonResourceRepository<DepositProduct, String> {
}
