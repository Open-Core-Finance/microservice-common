package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.product.dto.CryptoProductDto;
import tech.corefinance.product.entity.CryptoProduct;
import tech.corefinance.product.service.CryptoProductService;

@RestController
@RequestMapping("/crypto-products")
@ControllerManagedResource("cryptoproduct")
public class CryptoProductController implements CrudController<String, CryptoProduct, CryptoProductDto> {

    @Autowired
    private CryptoProductService cryptoProductService;

    @Override
    public CommonService<String, CryptoProduct, ?> getHandlingService() {
        return cryptoProductService;
    }
}
