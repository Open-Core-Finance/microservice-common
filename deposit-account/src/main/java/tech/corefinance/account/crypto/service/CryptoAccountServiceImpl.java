package tech.corefinance.account.crypto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.account.common.config.AccountKafkaConfig;
import tech.corefinance.account.common.service.AccountServiceImpl;
import tech.corefinance.account.crypto.dto.CreateCryptoAccountRequest;
import tech.corefinance.account.crypto.entity.CryptoAccount;
import tech.corefinance.account.crypto.repository.CryptoAccountRepository;
import tech.corefinance.account.deposit.service.DepositAccountProductMapper;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.jpa.repository.DbSequenceHandling;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.feign.client.product.CryptoProductClient;
import tech.corefinance.feign.client.product.ProductCategoryClient;
import tech.corefinance.feign.client.product.ProductTypeClient;
import tech.corefinance.product.common.dto.CryptoProductDto;

@Service
@Transactional
public class CryptoAccountServiceImpl extends AccountServiceImpl<CryptoAccount, CryptoAccountRepository>
        implements CryptoAccountService {

    private final CryptoAccountRepository cryptoAccountRepository;
    private final ProductCategoryClient productCategoryClient;
    private final ProductTypeClient productTypeClient;

    private final CryptoProductClient cryptoProductClient;

    @Autowired
    public CryptoAccountServiceImpl(@Value("${tech.corefinance.account.max-random-id-check:3}") int maxRandomIdCheck,
                                    TaskExecutor taskExecutor, DbSequenceHandling dbSequenceHandling,
                                    CryptoAccountRepository cryptoAccountRepository, ProductCategoryClient productCategoryClient,
                                    KafkaTemplate<String, Object> kafkaTemplate, AccountKafkaConfig accountKafkaConfig,
                                    ProductTypeClient productTypeClient, CryptoProductClient cryptoProductClient) {
        super(maxRandomIdCheck, taskExecutor, dbSequenceHandling, kafkaTemplate, accountKafkaConfig);
        this.cryptoAccountRepository = cryptoAccountRepository;
        this.productCategoryClient = productCategoryClient;
        this.productTypeClient = productTypeClient;
        this.cryptoProductClient = cryptoProductClient;
    }

    @Override
    public CryptoAccountRepository getRepository() {
        return cryptoAccountRepository;
    }

    @Override
    protected Object getCategoryObject(String categoryId) {
        return productCategoryClient.viewDetails(categoryId).getResult();
    }

    @Override
    protected Object getTypeObject(String typeId) {
        return productTypeClient.viewDetails(typeId).getResult();
    }

    @Override
    protected Object getProductObject(String productId) {
        return cryptoProductClient.viewDetails(productId).getResult();
    }

    @Override
    protected String getSequenceName() {
        return SEQUENCE_NAME;
    }

    @Override
    protected <D extends CreateUpdateDto<String>> CryptoAccount mapProductToAccount(D source, Object productObject, CryptoAccount dest) {
        dest = super.mapProductToAccount(source, productObject, dest);
        // Additional mapping
        if (productObject instanceof CryptoProductDto res) {
            dest = new DepositAccountProductMapper<>(source, dest, res).map();
            dest.setTermLength(((CreateCryptoAccountRequest) source).getTermLength());
        } else {
            throw new ServiceProcessingException("invalid_api_call");
        }
        // Return
        return dest;
    }
}
