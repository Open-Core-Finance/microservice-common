package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.product.dto.GlProductDto;
import tech.corefinance.product.entity.GlProduct;
import tech.corefinance.product.service.GlProductService;

@RestController
@RequestMapping("/gl-products")
@ControllerManagedResource("glproduct")
public class GlProductController implements CrudController<String, GlProduct, GlProductDto> {

    @Autowired
    private GlProductService glProductService;

    @Override
    public CommonService<String, GlProduct, ?> getHandlingService() {
        return glProductService;
    }
}
