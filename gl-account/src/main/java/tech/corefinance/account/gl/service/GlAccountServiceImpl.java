package tech.corefinance.account.gl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.account.common.config.AccountKafkaConfig;
import tech.corefinance.account.common.service.AccountServiceImpl;
import tech.corefinance.account.gl.entity.GlAccount;
import tech.corefinance.account.gl.repository.GlAccountRepository;
import tech.corefinance.common.jpa.repository.DbSequenceHandling;
import tech.corefinance.feign.client.product.GlProductClient;
import tech.corefinance.feign.client.product.ProductCategoryClient;
import tech.corefinance.feign.client.product.ProductTypeClient;

@Service
@Transactional
public class GlAccountServiceImpl extends AccountServiceImpl<GlAccount, GlAccountRepository> implements GlAccountService {

    private final GlAccountRepository glAccountRepository;
    private final ProductCategoryClient productCategoryClient;
    private final ProductTypeClient productTypeClient;
    private final GlProductClient glProductClient;

    @Autowired
    public GlAccountServiceImpl(@Value("${tech.corefinance.account.max-random-id-check:3}") int maxRandomIdCheck,
                                TaskExecutor taskExecutor, DbSequenceHandling dbSequenceHandling,
                                GlAccountRepository glAccountRepository, ProductCategoryClient productCategoryClient,
                                KafkaTemplate<String, Object> kafkaTemplate, AccountKafkaConfig accountKafkaConfig,
                                ProductTypeClient productTypeClient, GlProductClient glProductClient) {
        super(maxRandomIdCheck, taskExecutor, dbSequenceHandling, kafkaTemplate, accountKafkaConfig);
        this.glAccountRepository = glAccountRepository;
        this.glProductClient = glProductClient;
        this.productCategoryClient = productCategoryClient;
        this.productTypeClient = productTypeClient;
    }

    @Override
    public GlAccountRepository getRepository() {
        return glAccountRepository;
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
        return glProductClient.viewDetails(productId).getResult();
    }

    @Override
    protected String getSequenceName() {
        return SEQUENCE_NAME;
    }
}
