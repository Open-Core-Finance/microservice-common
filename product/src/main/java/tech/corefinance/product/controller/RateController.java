package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.product.entity.Rate;
import tech.corefinance.product.repository.RateRepository;

@RestController
@RequestMapping("/rates")
@ControllerManagedResource("rate")
public class RateController implements CrudServiceAndController<String, Rate, Rate, RateRepository> {

    @Autowired
    private RateRepository repository;

    @Override
    public RateRepository getRepository() {
        return repository;
    }
}
