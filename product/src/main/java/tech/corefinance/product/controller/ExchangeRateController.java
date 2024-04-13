package tech.corefinance.product.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.product.common.dto.ExchangeRateResponse;
import tech.corefinance.product.entity.ExchangeRate;
import tech.corefinance.product.entity.ExchangeRateId;
import tech.corefinance.product.repository.ExchangeRateRepository;

@RestController
@RequestMapping("/exchange-rates")
@ControllerManagedResource("exchangerate")
public class ExchangeRateController implements CrudServiceAndController<ExchangeRateId, ExchangeRate, ExchangeRate, ExchangeRateRepository> {

    @Autowired
    private ExchangeRateRepository repository;
    private final Converter<ExchangeRate, ?> converter = (ExchangeRate ech) -> {
        var exchangeRateResponse = new ExchangeRateResponse();
        BeanUtils.copyProperties(ech, exchangeRateResponse);
        exchangeRateResponse.setFromCurrency(ech.getId().getFromCurrency());
        exchangeRateResponse.setToCurrency(ech.getId().getToCurrency());
        return exchangeRateResponse;
    };

    @Override
    public ExchangeRateRepository getRepository() {
        return repository;
    }

    @Override
    public Converter<ExchangeRate, ?> getEntityConverter() {
        return converter;
    }
}
