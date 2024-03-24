package tech.corefinance.geocode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.geocode.entity.Region;
import tech.corefinance.geocode.repository.RegionRepository;

@RestController
@RequestMapping("/regions")
@ControllerManagedResource("region")
public class RegionController implements CrudServiceAndController<Integer, Region, Region, RegionRepository> {

    @Autowired
    private RegionRepository regionRepository;

    @Override
    public RegionRepository getRepository() {
        return regionRepository;
    }
}
