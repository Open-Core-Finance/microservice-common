package tech.corefinance.account.gl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.account.common.dto.GlTransactionRequest;
import tech.corefinance.account.gl.entity.GlTransaction;
import tech.corefinance.account.gl.service.GlTransactionService;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;

@RestController
@RequestMapping("/gl-transactions")
@ControllerManagedResource("gltransaction")
public class GlTransactionController implements CrudController<String, GlTransaction, GlTransactionRequest> {

    @Autowired
    private GlTransactionService glTransactionService;

    @Override
    public CommonService<String, GlTransaction, ?> getHandlingService() {
        return glTransactionService;
    }
}
