package tech.corefinance.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.customer.repository.IndividualCustomerRepository;

@Service
@Transactional
public class IndividualCustomerServiceImpl implements IndividualCustomerService {

    @Autowired
    private IndividualCustomerRepository individualCustomerRepository;

    @Override
    public IndividualCustomerRepository getRepository() {
        return individualCustomerRepository;
    }
}
