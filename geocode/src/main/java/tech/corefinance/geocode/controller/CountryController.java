package tech.corefinance.geocode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.geocode.entity.Country;
import tech.corefinance.geocode.service.CountryService;

@RestController
@RequestMapping("/countries")
@ControllerManagedResource("country")
public class CountryController implements CrudController<Integer, Country, Country> {

    @Autowired
    private CountryService countryService;

    @Override
    public CommonService<Integer, Country, ?> getHandlingService() {
        return countryService;
    }
}
