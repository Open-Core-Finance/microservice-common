package tech.corefinance.geocode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.geocode.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>, CommonResourceRepository<Country, Integer> {
}
