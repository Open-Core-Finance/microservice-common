package tech.corefinance.geocode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.geocode.entity.SubRegion;
import tech.corefinance.geocode.service.SubRegionService;

@RestController
@RequestMapping("/sub-regions")
@ControllerManagedResource("subRegion")
public class SubRegionController implements CrudController<Integer, SubRegion, SubRegion> {

    @Autowired
    private SubRegionService subRegionService;

    @Override
    public CommonService<Integer, SubRegion, ?> getHandlingService() {
        return subRegionService;
    }
}
