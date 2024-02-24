package tech.corefinance.customer.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.customer.entity.IndividualCustomer;
import tech.corefinance.customer.repository.IndividualCustomerRepository;

public interface IndividualCustomerService extends
        CommonService<Long, IndividualCustomer, IndividualCustomerRepository> {
}
