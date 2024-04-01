package tech.corefinance.geocode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.geocode.entity.State;
import tech.corefinance.geocode.repository.CountryRepository;
import tech.corefinance.geocode.repository.StateRepository;

@Service
@Transactional
public class StateServiceImpl implements StateService {
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Override
    public StateRepository getRepository() {
        return stateRepository;
    }

    @Override
    public <D extends CreateUpdateDto<Integer>> void copyAdditionalPropertiesFromDtoToEntity(D source, State dest) {
        StateService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        int countryId = dest.getCountryId();
        countryRepository.findById(countryId).ifPresent( c -> {
            dest.setCountry(c);
            dest.setCountryCode(c.getIso2());
        });
    }
}
