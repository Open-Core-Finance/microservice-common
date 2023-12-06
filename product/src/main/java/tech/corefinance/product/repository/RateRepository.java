package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, String>, CommonResourceRepository<Rate, String> {
}
