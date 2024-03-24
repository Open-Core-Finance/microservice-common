package tech.corefinance.geocode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.geocode.entity.SubRegion;

@Repository
public interface SubRegionRepository extends JpaRepository<SubRegion, Integer>, CommonResourceRepository<SubRegion, Integer> {
}
