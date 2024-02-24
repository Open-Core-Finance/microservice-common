package tech.corefinance.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.customer.entity.IndividualCustomer;
import tech.corefinance.customer.service.IndividualCustomerService;

@RestController
@RequestMapping("/individual-customers")
@ControllerManagedResource("individualcustomer")
public class IndividualCustomerController implements CrudController<Long, IndividualCustomer, IndividualCustomer> {

    @Autowired
    private IndividualCustomerService individualCustomerService;

    @Override
    public CommonService<Long, IndividualCustomer, ?> getHandlingService() {
        return individualCustomerService;
    }
}
