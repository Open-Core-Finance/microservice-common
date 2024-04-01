package tech.corefinance.geocode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.geocode.entity.Region;
import tech.corefinance.geocode.entity.SubRegion;
import tech.corefinance.geocode.repository.RegionRepository;
import tech.corefinance.geocode.repository.SubRegionRepository;

import java.util.Optional;

@Service
@Transactional
public class SubRegionServiceImpl implements SubRegionService {

    @Autowired
    private SubRegionRepository subRegionRepository;
    @Autowired
    private RegionRepository regionRepository;

    @Override
    public SubRegionRepository getRepository() {
        return subRegionRepository;
    }

    @Override
    public <D extends CreateUpdateDto<Integer>> void copyAdditionalPropertiesFromDtoToEntity(D source, SubRegion dest) {
        SubRegionService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        int regionId = dest.getRegionId();
        Optional<Region> optionalRegion = regionRepository.findById(regionId);
        optionalRegion.ifPresent(dest::setRegion);
    }
}
