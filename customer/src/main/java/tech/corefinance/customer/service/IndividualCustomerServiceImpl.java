package tech.corefinance.customer.service;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.customer.entity.IndividualCustomer;
import tech.corefinance.customer.repository.IndividualCustomerRepository;
import tech.corefinance.feign.client.geocode.CityClient;
import tech.corefinance.feign.client.geocode.CountryClient;
import tech.corefinance.feign.client.geocode.StateClient;

@Service
@Transactional
public class IndividualCustomerServiceImpl extends CustomerServiceImpl implements IndividualCustomerService {

    private IndividualCustomerRepository individualCustomerRepository;

    public IndividualCustomerServiceImpl(IndividualCustomerRepository individualCustomerRepository, CityClient cityClient,
                                         TaskExecutor taskExecutor, StateClient stateClient, CountryClient countryClient) {
        super(cityClient, taskExecutor, stateClient, countryClient);
        this.individualCustomerRepository = individualCustomerRepository;
    }

    @Override
    public IndividualCustomerRepository getRepository() {
        return individualCustomerRepository;
    }

    @Override
    public <D extends CreateUpdateDto<Long>> void copyAdditionalPropertiesFromDtoToEntity(D source, IndividualCustomer dest) {
        IndividualCustomerService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        this.validateCreateCustomer(dest);
        if (dest.isSingleNationality()) {
            dest.setSecondNationality(null);
        }
    }
}
