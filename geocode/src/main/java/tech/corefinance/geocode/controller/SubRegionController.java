package tech.corefinance.geocode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.geocode.entity.SubRegion;
import tech.corefinance.geocode.repository.SubRegionRepository;

@RestController
@RequestMapping("/sub-regions")
@ControllerManagedResource("subRegion")
public class SubRegionController implements CrudServiceAndController<Integer, SubRegion, SubRegion, SubRegionRepository> {

    @Autowired
    private SubRegionRepository subRegionRepository;

    @Override
    public SubRegionRepository getRepository() {
        return subRegionRepository;
    }
}
