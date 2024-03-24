package tech.corefinance.geocode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.geocode.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Integer>, CommonResourceRepository<State, Integer> {
}
