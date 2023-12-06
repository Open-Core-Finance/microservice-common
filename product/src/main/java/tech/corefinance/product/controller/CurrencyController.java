package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.product.entity.Currency;
import tech.corefinance.product.repository.CurrencyRepository;

@RestController
@RequestMapping("/currencies")
@ControllerManagedResource("currency")
public class CurrencyController implements CrudServiceAndController<String, Currency, Currency, CurrencyRepository> {

    @Autowired
    private CurrencyRepository repository;

    @Override
    public CurrencyRepository getRepository() {
        return repository;
    }
}
