package tech.corefinance.geocode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.geocode.entity.State;
import tech.corefinance.geocode.service.StateService;

@RestController
@RequestMapping("/states")
@ControllerManagedResource("state")
public class StateController implements CrudController<Integer, State, State> {

    @Autowired
    private StateService stateService;

    @Override
    public CommonService<Integer, State, ?> getHandlingService() {
        return stateService;
    }
}
