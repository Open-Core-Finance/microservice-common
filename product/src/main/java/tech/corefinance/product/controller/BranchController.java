package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.product.entity.Branch;
import tech.corefinance.product.repository.BranchRepository;

@RestController
@RequestMapping("/branches")
@ControllerManagedResource("branch")
public class BranchController implements CrudServiceAndController<String, Branch, Branch, BranchRepository> {

    @Autowired
    private BranchRepository repository;

    @Override
    public BranchRepository getRepository() {
        return repository;
    }
}
