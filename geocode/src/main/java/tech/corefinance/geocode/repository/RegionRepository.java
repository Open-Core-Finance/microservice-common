package tech.corefinance.geocode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.geocode.entity.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer>, CommonResourceRepository<Region, Integer> {
}
