package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.product.entity.ExchangeRate;
import tech.corefinance.product.repository.ExchangeRateRepository;

@RestController
@RequestMapping("/exchange-rates")
@ControllerManagedResource("exchangerate")
public class ExchangeRateController implements CrudServiceAndController<String, ExchangeRate, ExchangeRate, ExchangeRateRepository> {

    @Autowired
    private ExchangeRateRepository repository;

    @Override
    public ExchangeRateRepository getRepository() {
        return repository;
    }
}
