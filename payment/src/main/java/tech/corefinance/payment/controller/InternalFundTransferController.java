package tech.corefinance.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.payment.entity.InternalFundTransfer;
import tech.corefinance.payment.service.InternalFundTransferService;

@RestController
@RequestMapping("/internal-fund-transfers")
@ControllerManagedResource("internalFundTransfer")
public class InternalFundTransferController implements CrudController<String, InternalFundTransfer, InternalFundTransfer> {

    @Autowired
    private InternalFundTransferService internalFundTransferService;

    @Override
    public CommonService<String, InternalFundTransfer, ?> getHandlingService() {
        return internalFundTransferService;
    }
}
