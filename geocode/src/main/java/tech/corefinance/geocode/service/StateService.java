package tech.corefinance.geocode.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.geocode.entity.State;
import tech.corefinance.geocode.repository.StateRepository;

public interface StateService extends CommonService<Integer, State, StateRepository> {
}
