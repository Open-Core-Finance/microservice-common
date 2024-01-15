package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.ProductCategory;
import tech.corefinance.product.enums.ProductCategoryType;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String>,
        CommonResourceRepository<ProductCategory, String> {

    List<ProductCategory> findAllByType(ProductCategoryType categoryType);
}
