package tech.corefinance.geocode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.geocode.entity.City;
import tech.corefinance.geocode.service.CityService;

@RestController
@RequestMapping("/cities")
@ControllerManagedResource("city")
public class CityController implements CrudController<Integer, City, City> {

    @Autowired
    private CityService cityService;

    @Override
    public CommonService<Integer, City, ?> getHandlingService() {
        return cityService;
    }
}
