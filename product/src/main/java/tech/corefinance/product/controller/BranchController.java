package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.product.entity.Branch;
import tech.corefinance.product.service.BranchService;

@RestController
@RequestMapping("/branches")
@ControllerManagedResource("branch")
public class BranchController implements CrudController<String, Branch, Branch> {

    @Autowired
    private BranchService branchService;

    @Override
    public CommonService<String, Branch, ?> getHandlingService() {
        return branchService;
    }
}
