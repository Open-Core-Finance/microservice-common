package tech.corefinance.product.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.product.common.enums.CreditArrangementManaged;
import tech.corefinance.product.common.enums.FrequencyOptionYearly;
import tech.corefinance.product.common.model.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class GenericDepositProductDto extends ProductDto {

    private List<DepositProductFee> productFees;

    /**
     * Interest Rate.
     */
    private boolean enableInterestRate;
    private DepositProductInterestRate interestRate;

    // Internal control
    private boolean autoSetAsDormant;
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
    private boolean enableEarlyClosurePeriod;
    private Integer earlyClosurePeriod;

    private Boolean allowOverdrafts;
    private DepositProductInterestRate overdraftsInterest;
    private List<CurrencyValue> maxOverdraftLimits;
    private CreditArrangementManaged overdraftsUnderCreditArrangementManaged;

    private boolean enableTermDeposit;
    private FrequencyOptionYearly termUnit;
    private Integer minTermLength;
    private Integer maxTermLength;
    private Integer defaultTermLength;
}
