package tech.corefinance.geocode.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.geocode.entity.Country;
import tech.corefinance.geocode.repository.CountryRepository;

public interface CountryService extends CommonService<Integer, Country, CountryRepository> {
}
