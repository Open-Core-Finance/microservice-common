package tech.corefinance.geocode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.geocode.entity.State;
import tech.corefinance.geocode.repository.StateRepository;

@RestController
@RequestMapping("/states")
@ControllerManagedResource("state")
public class StateController implements CrudServiceAndController<Integer, State, State, StateRepository> {

    @Autowired
    private StateRepository stateRepository;

    @Override
    public StateRepository getRepository() {
        return stateRepository;
    }
}
