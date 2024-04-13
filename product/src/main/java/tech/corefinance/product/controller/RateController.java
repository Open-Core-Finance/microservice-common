package tech.corefinance.product.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.product.common.dto.RateResponse;
import tech.corefinance.product.entity.Rate;
import tech.corefinance.product.service.RateService;

@RestController
@RequestMapping("/rates")
@ControllerManagedResource("rate")
public class RateController implements CrudController<String, Rate, RateResponse> {

    @Autowired
    private RateService rateService;
    private Converter<Rate, RateResponse> converter = (Rate source) -> {
        var response = new RateResponse();
        BeanUtils.copyProperties(source, response);
        if (source.getRateSource() != null) {
            response.setRateSourceId(source.getRateSource().getId());
            response.setRateSourceName(source.getRateSource().getName());
        }
        return response;
    };

    @Override
    public CommonService<String, Rate, ?> getHandlingService() {
        return rateService;
    }

    @Override
    public Converter<Rate, ?> getEntityConverter() {
        return converter;
    }
}
