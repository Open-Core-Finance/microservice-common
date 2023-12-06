package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.Holiday;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, String>,
        CommonResourceRepository<Holiday, String> {
}
