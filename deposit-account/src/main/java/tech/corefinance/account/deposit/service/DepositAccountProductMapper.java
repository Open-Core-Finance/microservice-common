package tech.corefinance.account.deposit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import tech.corefinance.account.common.model.DepositAccountInterestRate;
import tech.corefinance.account.deposit.dto.GenericCreateDepositAccountRequest;
import tech.corefinance.account.deposit.entity.GenericDepositAccount;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.product.common.dto.GenericDepositProductDto;
import tech.corefinance.product.common.model.DepositProductInterestRate;

@Slf4j
public class DepositAccountProductMapper<T extends GenericDepositAccount, D extends CreateUpdateDto<String>> {

    private final T depositAccount;
    private final D source;
    private final GenericDepositProductDto depositProductResponse;

    public DepositAccountProductMapper(D source, T depositAccount, GenericDepositProductDto depositProductResponse) {
        this.source = source;
        this.depositAccount = depositAccount;
        this.depositProductResponse = depositProductResponse;
    }

    public T map() {
        var product = depositProductResponse;
        depositAccount.setAllowDepositAfterMaturityDate(product.isAllowDepositAfterMaturityDate());
        depositAccount.setAccountFees(product.getProductFees());

        var productInterestRate = product.getInterestRate();
        log.debug("Product interest rate [{}]", productInterestRate);
        var accountInterestRate = createInterestRate(productInterestRate);
        depositAccount.setInterestRate(accountInterestRate);
        if (source instanceof GenericCreateDepositAccountRequest request) {
            accountInterestRate.setInterestRateValues(request.getInterestRateValues());
            accountInterestRate.setInterestItems(request.getInterestItems());
        }
        log.debug("Converted account interest rate [{}]", productInterestRate);

        depositAccount.setDaysToSetToDormant(product.getDaysToSetToDormant());
        depositAccount.setDepositLimits(product.getDepositLimits());
        depositAccount.setWithdrawalLimits(product.getWithdrawalLimits());
        depositAccount.setEarlyClosurePeriod(product.getEarlyClosurePeriod());
        depositAccount.setAllowOverdrafts(product.getAllowOverdrafts());
        depositAccount.setOverdraftsInterest(createInterestRate(product.getOverdraftsInterest()));
        depositAccount.setMaxOverdraftLimits(product.getMaxOverdraftLimits());
        depositAccount.setOverdraftsUnderCreditArrangementManaged(product.getOverdraftsUnderCreditArrangementManaged());
        depositAccount.setEnableTermDeposit(product.isEnableTermDeposit());
        depositAccount.setTermUnit(product.getTermUnit());
        return depositAccount;
    }

    private DepositAccountInterestRate createInterestRate(DepositProductInterestRate productInterest) {
        var result = new DepositAccountInterestRate();
        if (productInterest != null) {
            BeanUtils.copyProperties(productInterest, result);
        }
        return result;
    }
}
