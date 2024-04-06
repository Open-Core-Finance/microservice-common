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
    public <D extends CreateUpdateDto<Integer>> State copyAdditionalPropertiesFromDtoToEntity(D source, State dest) {
        var result = StateService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        int countryId = result.getCountryId();
        countryRepository.findById(countryId).ifPresent( c -> {
            result.setCountry(c);
            result.setCountryCode(c.getIso2());
        });
        return result;
    }
}
