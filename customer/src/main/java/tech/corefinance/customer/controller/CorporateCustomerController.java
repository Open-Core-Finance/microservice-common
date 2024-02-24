package tech.corefinance.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.customer.entity.CorporateCustomer;
import tech.corefinance.customer.service.CorporateCustomerService;

@RestController
@RequestMapping("/corporate-customers")
@ControllerManagedResource("corporatecustomer")
public class CorporateCustomerController implements CrudController<Long, CorporateCustomer, CorporateCustomer> {

    @Autowired
    private CorporateCustomerService corporateCustomerService;

    @Override
    public CommonService<Long, CorporateCustomer, ?> getHandlingService() {
        return corporateCustomerService;
    }
}
