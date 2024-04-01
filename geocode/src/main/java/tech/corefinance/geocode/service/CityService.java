package tech.corefinance.geocode.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.geocode.entity.City;
import tech.corefinance.geocode.repository.CityRepository;

public interface CityService extends CommonService<Integer, City, CityRepository> {
}
