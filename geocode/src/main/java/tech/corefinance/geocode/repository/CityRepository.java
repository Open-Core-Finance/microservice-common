package tech.corefinance.geocode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.geocode.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer>, CommonResourceRepository<City, Integer> {
}
