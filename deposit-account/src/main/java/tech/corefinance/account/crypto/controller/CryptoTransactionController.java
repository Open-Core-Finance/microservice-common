package tech.corefinance.account.crypto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.account.crypto.dto.CryptoTransactionRequest;
import tech.corefinance.account.crypto.entity.CryptoTransaction;
import tech.corefinance.account.crypto.service.CryptoTransactionService;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;

@RestController
@RequestMapping("/crypto-transactions")
@ControllerManagedResource("cryptoTransaction")
public class CryptoTransactionController implements CrudController<String, CryptoTransaction, CryptoTransactionRequest> {

    @Autowired
    private CryptoTransactionService cryptoTransactionService;

    @Override
    public CommonService<String, CryptoTransaction, ?> getHandlingService() {
        return cryptoTransactionService;
    }
}
