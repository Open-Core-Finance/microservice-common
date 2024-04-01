package tech.corefinance.customer.service;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.customer.entity.CorporateCustomer;
import tech.corefinance.customer.repository.CorporateCustomerRepository;
import tech.corefinance.feign.client.geocode.CityClient;
import tech.corefinance.feign.client.geocode.CountryClient;
import tech.corefinance.feign.client.geocode.StateClient;

@Service
@Transactional
public class CorporateCustomerServiceImpl extends CustomerServiceImpl implements CorporateCustomerService {

    private CorporateCustomerRepository corporateCustomerRepository;

    public CorporateCustomerServiceImpl(CorporateCustomerRepository corporateCustomerRepository, CityClient cityClient,
                                        TaskExecutor taskExecutor, StateClient stateClient, CountryClient countryClient) {
        super(cityClient, taskExecutor, stateClient, countryClient);
        this.corporateCustomerRepository = corporateCustomerRepository;
    }

    @Override
    public CorporateCustomerRepository getRepository() {
        return corporateCustomerRepository;
    }

    @Override
    public <D extends CreateUpdateDto<Long>> void copyAdditionalPropertiesFromDtoToEntity(D source, CorporateCustomer dest) {
        CorporateCustomerService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        this.validateCreateCustomer(dest);
    }
}
