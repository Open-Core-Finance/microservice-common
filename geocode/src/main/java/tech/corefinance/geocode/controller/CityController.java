package tech.corefinance.geocode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.geocode.entity.City;
import tech.corefinance.geocode.repository.CityRepository;

@RestController
@RequestMapping("/cities")
@ControllerManagedResource("city")
public class CityController implements CrudServiceAndController<Integer, City, City, CityRepository> {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public CityRepository getRepository() {
        return cityRepository;
    }
}
