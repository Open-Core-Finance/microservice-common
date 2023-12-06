package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.product.entity.DepositProduct;
import tech.corefinance.product.repository.DepositProductRepository;

@RestController
@RequestMapping("/deposit-products")
@ControllerManagedResource("depositproduct")
public class DepositProductController implements CrudServiceAndController<String, DepositProduct, DepositProduct, DepositProductRepository> {

    @Autowired
    private DepositProductRepository repository;

    @Override
    public DepositProductRepository getRepository() {
        return repository;
    }
}
