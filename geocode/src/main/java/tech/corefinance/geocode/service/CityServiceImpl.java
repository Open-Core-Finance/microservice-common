package tech.corefinance.geocode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.geocode.entity.City;
import tech.corefinance.geocode.repository.CityRepository;
import tech.corefinance.geocode.repository.CountryRepository;
import tech.corefinance.geocode.repository.StateRepository;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Override
    public CityRepository getRepository() {
        return cityRepository;
    }

    @Override
    public <D extends CreateUpdateDto<Integer>> City copyAdditionalPropertiesFromDtoToEntity(D source, City dest) {
        var result = CityService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        int stateId = result.getStateId();
        stateRepository.findById(stateId).ifPresent( s -> {
            result.setState(s);
            result.setStateCode(s.getIso2());
            int countryId = s.getCountryId();
            countryRepository.findById(countryId).ifPresent( c -> {
                result.setCountry(c);
                result.setCountryCode(c.getIso2());
            });
        });
        return result;
    }
}
