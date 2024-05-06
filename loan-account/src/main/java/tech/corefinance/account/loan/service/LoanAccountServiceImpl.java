package tech.corefinance.account.loan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.account.common.config.AccountKafkaConfig;
import tech.corefinance.account.common.model.AccountArrearsSetting;
import tech.corefinance.account.common.model.AccountPenaltySetting;
import tech.corefinance.account.common.model.AccountRepaymentScheduling;
import tech.corefinance.account.common.model.LoanAccountInterestRate;
import tech.corefinance.account.common.service.AccountServiceImpl;
import tech.corefinance.account.loan.dto.CreateLoanAccountRequest;
import tech.corefinance.account.loan.entity.LoanAccount;
import tech.corefinance.account.loan.repository.LoanAccountRepository;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.jpa.repository.DbSequenceHandling;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.feign.client.product.LoanProductClient;
import tech.corefinance.feign.client.product.ProductCategoryClient;
import tech.corefinance.feign.client.product.ProductTypeClient;
import tech.corefinance.product.common.dto.LoanProductDto;
import tech.corefinance.product.common.model.ArrearsSetting;
import tech.corefinance.product.common.model.LoanProductInterestRate;
import tech.corefinance.product.common.model.PenaltySetting;
import tech.corefinance.product.common.model.RepaymentScheduling;

@Service
@Transactional
@Slf4j
public class LoanAccountServiceImpl extends AccountServiceImpl<LoanAccount, LoanAccountRepository> implements LoanAccountService {

    private final LoanAccountRepository loanAccountRepository;
    private final LoanProductClient loanProductClient;
    private final ProductCategoryClient productCategoryClient;
    private final ProductTypeClient productTypeClient;

    @Autowired
    public LoanAccountServiceImpl(@Value("${tech.corefinance.account.max-random-id-check:3}") int maxRandomIdCheck,
                                  TaskExecutor taskExecutor, DbSequenceHandling dbSequenceHandling,
                                  LoanAccountRepository loanAccountRepository, ProductCategoryClient productCategoryClient,
                                  KafkaTemplate<String, Object> kafkaTemplate, AccountKafkaConfig accountKafkaConfig,
                                     ProductTypeClient productTypeClient, LoanProductClient loanProductClient) {
        super(maxRandomIdCheck, taskExecutor, dbSequenceHandling, kafkaTemplate, accountKafkaConfig);
        this.loanAccountRepository = loanAccountRepository;
        this.productCategoryClient = productCategoryClient;
        this.productTypeClient = productTypeClient;
        this.loanProductClient = loanProductClient;
    }

    @Override
    public LoanAccountRepository getRepository() {
        return loanAccountRepository;
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
        return loanProductClient.viewDetails(productId).getResult();
    }

    @Override
    protected String getSequenceName() {
        return SEQUENCE_NAME;
    }

    @Override
    protected <D extends CreateUpdateDto<String>> LoanAccount mapProductToAccount(D source, Object productObject,
                                                                                  LoanAccount dest) {
        dest = super.mapProductToAccount(source, productObject, dest);
        // Additional mapping
        if (productObject instanceof LoanProductDto product && source instanceof CreateLoanAccountRequest request) {

            var productInterestRate = product.getInterestRate();
            log.debug("Product interest rate [{}]", productInterestRate);
            if (productInterestRate != null) {
                var accountInterestRate = createInterestRate(productInterestRate);
                dest.setAccountInterestRate(accountInterestRate);
                accountInterestRate.setInterestRateValues(request.getInterestRateValues());
                log.debug("Converted account interest rate [{}]", accountInterestRate);
            }

            var productRepaymentScheduling = product.getRepaymentScheduling();
            log.debug("Product repayment scheduling [{}]", productRepaymentScheduling);
            if (productRepaymentScheduling != null) {
                var accountRepaymentScheduling = createRepaymentScheduling(productRepaymentScheduling);
                dest.setAccountRepaymentScheduling(accountRepaymentScheduling);
                accountRepaymentScheduling.setInstallmentsValues(request.getInstallmentsValues());
                accountRepaymentScheduling.setFirstDueDateOffsetValues(request.getFirstDueDateOffsetValues());
                accountRepaymentScheduling.setGracePeriodValues(request.getGracePeriodValues());
                log.debug("Converted account repayment scheduling [{}]", accountRepaymentScheduling);
            }

            var productArrearsSetting = product.getArrearsSetting();
            log.debug("Product arrears setting [{}]", productArrearsSetting);
            if (productArrearsSetting != null) {
                var accountArrearsSetting = createArrearsSetting(productArrearsSetting);
                dest.setAccountArrearsSetting(accountArrearsSetting);
                accountArrearsSetting.setTolerancePeriods(request.getTolerancePeriods());
                accountArrearsSetting.setToleranceAmounts(request.getToleranceAmounts());
                log.debug("Converted account arrears setting [{}]", accountArrearsSetting);
            }

            var productPenaltySetting = product.getPenaltySetting();
            log.debug("Product penalty setting [{}]", productPenaltySetting);
            if (productPenaltySetting != null) {
                var accountPenaltySetting = createPenaltySetting(productPenaltySetting);
                dest.setAccountPenaltySetting(accountPenaltySetting);
                accountPenaltySetting.setPenaltyRateValues(request.getPenaltyRateValues());
                log.debug("Converted account penalty setting [{}]", accountPenaltySetting);
            }

            dest.setRepaymentCollection(product.getRepaymentCollection());
        } else {
            throw new ServiceProcessingException("invalid_api_call");
        }
        // Return
        return dest;
    }

    private LoanAccountInterestRate createInterestRate(LoanProductInterestRate productInterest) {
        var result = new LoanAccountInterestRate();
        if (productInterest != null) {
            BeanUtils.copyProperties(productInterest, result);
        }
        return result;
    }

    private AccountRepaymentScheduling createRepaymentScheduling(RepaymentScheduling productRepaymentScheduling) {
        var result = new AccountRepaymentScheduling();
        if (productRepaymentScheduling != null) {
            BeanUtils.copyProperties(productRepaymentScheduling, result);
        }
        return result;
    }

    private AccountArrearsSetting createArrearsSetting(ArrearsSetting productArrearsSetting) {
        var result = new AccountArrearsSetting();
        if (productArrearsSetting != null) {
            BeanUtils.copyProperties(productArrearsSetting, result);
        }
        return result;
    }

    private AccountPenaltySetting createPenaltySetting(PenaltySetting productPenaltySetting) {
        var result = new AccountPenaltySetting();
        if (productPenaltySetting != null) {
            BeanUtils.copyProperties(productPenaltySetting, result);
        }
        return result;
    }
}
