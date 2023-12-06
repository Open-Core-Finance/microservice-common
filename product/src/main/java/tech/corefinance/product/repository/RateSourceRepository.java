package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.RateSource;

@Repository
public interface RateSourceRepository extends JpaRepository<RateSource, String>,
        CommonResourceRepository<RateSource, String> {
}
