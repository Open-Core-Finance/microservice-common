package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.CryptoProduct;

@Repository
public interface CryptoProductRepository extends JpaRepository<CryptoProduct, String>,
        CommonResourceRepository<CryptoProduct, String> {
}
