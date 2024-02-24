package tech.corefinance.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.customer.entity.CorporateCustomer;

@Repository
public interface CorporateCustomerRepository extends JpaRepository<CorporateCustomer, Long>,
        CommonResourceRepository<CorporateCustomer, Long> {
}
