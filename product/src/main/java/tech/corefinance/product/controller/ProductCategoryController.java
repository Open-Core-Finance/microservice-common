package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.product.entity.ProductCategory;
import tech.corefinance.product.repository.ProductCategoryRepository;

@RestController
@RequestMapping("/product-categories")
@ControllerManagedResource("productcategory")
public class ProductCategoryController implements CrudServiceAndController<String, ProductCategory, ProductCategory, ProductCategoryRepository> {

    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public ProductCategoryRepository getRepository() {
        return repository;
    }
}
