package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.GlProduct;

@Repository
public interface GlProductRepository extends JpaRepository<GlProduct, String>,
        CommonResourceRepository<GlProduct, String> {
}
