package tech.corefinance.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.customer.repository.CorporateCustomerRepository;

@Service
@Transactional
public class CorporateCustomerServiceImpl implements CorporateCustomerService {

    @Autowired
    private CorporateCustomerRepository corporateCustomerRepository;

    @Override
    public CorporateCustomerRepository getRepository() {
        return corporateCustomerRepository;
    }
}
