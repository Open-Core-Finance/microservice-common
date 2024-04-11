package tech.corefinance.account.deposit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.account.common.service.AccountServiceImpl;
import tech.corefinance.account.deposit.dto.CreateDepositAccountRequest;
import tech.corefinance.account.deposit.entity.DepositAccount;
import tech.corefinance.account.deposit.repository.DepositAccountRepository;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.jpa.repository.DbSequenceHandling;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.feign.client.product.DepositProductClient;
import tech.corefinance.feign.client.product.ProductCategoryClient;
import tech.corefinance.feign.client.product.ProductTypeClient;
import tech.corefinance.feign.client.product.entity.DepositProductResponse;

@Service
@Transactional
public class DepositAccountServiceImpl extends AccountServiceImpl<DepositAccount, DepositAccountRepository>
        implements DepositAccountService {

    private final DepositAccountRepository depositAccountRepository;
    private final ProductCategoryClient productCategoryClient;
    private final ProductTypeClient productTypeClient;

    private final DepositProductClient depositProductClient;

    @Autowired
    public DepositAccountServiceImpl(@Value("${tech.corefinance.account.max-random-id-check:3}") int maxRandomIdCheck,
                                     TaskExecutor taskExecutor, DbSequenceHandling dbSequenceHandling,
                                     DepositAccountRepository depositAccountRepository,
                                     ProductCategoryClient productCategoryClient,
                                     ProductTypeClient productTypeClient, DepositProductClient depositProductClient) {
        super(maxRandomIdCheck, taskExecutor, dbSequenceHandling);
        this.depositAccountRepository = depositAccountRepository;
        this.productCategoryClient = productCategoryClient;
        this.productTypeClient = productTypeClient;
        this.depositProductClient = depositProductClient;
    }

    @Override
    public DepositAccountRepository getRepository() {
        return depositAccountRepository;
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
        return depositProductClient.viewDetails(productId).getResult();
    }

    @Override
    protected String getSequenceName() {
        return SEQUENCE_NAME;
    }

    @Override
    protected <D extends CreateUpdateDto<String>> DepositAccount mapProductToAccount(D source, Object productObject,
                                                                                     DepositAccount dest) {
        dest = super.mapProductToAccount(source, productObject, dest);
        // Additional mapping
        if (productObject instanceof DepositProductResponse res) {
            dest = new DepositAccountProductMapper<>(source, dest, res).map();
            dest.setTermLength(((CreateDepositAccountRequest) source).getTermLength());
        } else {
            throw new ServiceProcessingException("invalid_api_call");
        }
        // Return
        return dest;
    }
}
