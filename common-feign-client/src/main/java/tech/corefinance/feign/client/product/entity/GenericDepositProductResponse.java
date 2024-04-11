package tech.corefinance.feign.client.product.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.product.common.enums.CreditArrangementManaged;
import tech.corefinance.product.common.enums.FrequencyOptionYearly;
import tech.corefinance.product.common.model.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class GenericDepositProductResponse extends Product {

    private List<DepositProductFee> productFees;

    private boolean allowDepositAfterMaturityDate;

    /**
     * Interest Rate.
     */
    private DepositProductInterestRate interestRate;

    // Internal control
    private Integer daysToSetToDormant;

    /**
     * Deposit transaction limits.
     */
    private List<DepositLimit> depositLimits;
    /**
     * Withdrawal Limits.
     */
    private List<WithdrawalLimit> withdrawalLimits;
    /**
     * Early Closure Period.
     */
    private Integer earlyClosurePeriod;

    private Boolean allowOverdrafts;
    private DepositProductInterestRate overdraftsInterest;
    private List<CurrencyValue> maxOverdraftLimit;
    private CreditArrangementManaged overdraftsUnderCreditArrangementManaged;

    private boolean enableTermDeposit;
    private FrequencyOptionYearly termUnit;
    private Integer minTermLength;
    private Integer maxTermLength;
    private Integer defaultTermLength;
}
