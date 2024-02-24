package tech.corefinance.customer.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.customer.entity.CorporateCustomer;
import tech.corefinance.customer.repository.CorporateCustomerRepository;

public interface CorporateCustomerService extends
        CommonService<Long, CorporateCustomer, CorporateCustomerRepository> {
}
