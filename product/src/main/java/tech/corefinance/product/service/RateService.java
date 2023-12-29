package tech.corefinance.product.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.product.entity.Rate;
import tech.corefinance.product.repository.RateRepository;

public interface RateService extends CommonService<String, Rate, RateRepository> {
}
