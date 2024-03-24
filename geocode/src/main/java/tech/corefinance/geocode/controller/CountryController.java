package tech.corefinance.geocode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.geocode.entity.Country;
import tech.corefinance.geocode.repository.CountryRepository;

@RestController
@RequestMapping("/countries")
@ControllerManagedResource("country")
public class CountryController implements CrudServiceAndController<Integer, Country, Country, CountryRepository> {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public CountryRepository getRepository() {
        return countryRepository;
    }
}
