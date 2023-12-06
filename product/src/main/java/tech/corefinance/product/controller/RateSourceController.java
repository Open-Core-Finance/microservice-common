package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.product.entity.RateSource;
import tech.corefinance.product.repository.RateSourceRepository;

@RestController
@RequestMapping("/rate-sources")
@ControllerManagedResource("ratesource")
public class RateSourceController implements CrudServiceAndController<String, RateSource, RateSource, RateSourceRepository> {

    @Autowired
    private RateSourceRepository repository;

    @Override
    public RateSourceRepository getRepository() {
        return repository;
    }
}
