package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.product.dto.LoanProductDto;
import tech.corefinance.product.entity.LoanProduct;
import tech.corefinance.product.repository.LoanProductRepository;
import tech.corefinance.product.service.LoanProductService;

@RestController
@RequestMapping("/loan-products")
@ControllerManagedResource("loanproduct")
public class LoanProductController implements CrudController<String, LoanProduct, LoanProductDto> {

    @Autowired
    private LoanProductService loanProductService;


    @Override
    public CommonService<String, LoanProduct, ?> getHandlingService() {
        return loanProductService;
    }
}
