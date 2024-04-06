package tech.corefinance.geocode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.geocode.entity.Country;
import tech.corefinance.geocode.entity.Region;
import tech.corefinance.geocode.repository.CountryRepository;
import tech.corefinance.geocode.repository.RegionRepository;
import tech.corefinance.geocode.repository.SubRegionRepository;

import java.util.Optional;

@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private SubRegionRepository subRegionRepository;
    @Autowired
    private RegionRepository regionRepository;

    @Override
    public CountryRepository getRepository() {
        return countryRepository;
    }

    @Override
    public <D extends CreateUpdateDto<Integer>> Country copyAdditionalPropertiesFromDtoToEntity(D source, Country dest) {
        dest = CountryService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        Integer subRegionId = dest.getSubRegionId();
        if (subRegionId != null) {
            subRegionRepository.findById(subRegionId).ifPresent(dest::setSubRegion);
        }
        Integer regionId = dest.getRegionId();
        if (regionId != null) {
            regionRepository.findById(regionId).ifPresent(dest::setRegion);
        }
        return dest;
    }
}
