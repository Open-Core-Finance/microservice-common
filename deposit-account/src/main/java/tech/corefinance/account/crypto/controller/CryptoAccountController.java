package tech.corefinance.account.crypto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.account.crypto.dto.CreateCryptoAccountRequest;
import tech.corefinance.account.crypto.entity.CryptoAccount;
import tech.corefinance.account.crypto.service.CryptoAccountService;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.service.CommonService;

@RestController
@RequestMapping("/crypto-accounts")
@ControllerManagedResource("cryptoAccount")
public class CryptoAccountController implements CrudController<String, CryptoAccount, CreateCryptoAccountRequest> {

    @Autowired
    private CryptoAccountService cryptoAccountService;

    @Override
    public CommonService<String, CryptoAccount, ?> getHandlingService() {
        return cryptoAccountService;
    }
}
