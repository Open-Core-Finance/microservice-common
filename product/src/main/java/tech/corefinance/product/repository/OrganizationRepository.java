package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String>,
        CommonResourceRepository<Organization, String> {
}
