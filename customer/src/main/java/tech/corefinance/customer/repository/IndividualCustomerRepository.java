package tech.corefinance.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.customer.entity.IndividualCustomer;

@Repository
public interface IndividualCustomerRepository extends JpaRepository<IndividualCustomer, Long>,
        CommonResourceRepository<IndividualCustomer, Long> {
}
