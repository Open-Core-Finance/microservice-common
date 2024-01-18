package tech.corefinance.product.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.product.enums.CreditArrangementManaged;
import tech.corefinance.product.enums.FrequencyOptionYearly;
import tech.corefinance.product.model.CurrencyLimitValue;
import tech.corefinance.product.model.DepositInterestRate;
import tech.corefinance.product.model.DepositLimit;
import tech.corefinance.product.model.WithdrawalLimit;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepositProductDto extends ProductDto {
    /**
     * Interest Rate.
     */
    private boolean enableInterestRate;
    private DepositInterestRate interestRate;

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
    private DepositInterestRate overdraftsInterest;
    private List<CurrencyLimitValue> maxOverdraftLimits;
    private CreditArrangementManaged overdraftsUnderCreditArrangementManaged;

    private boolean enableTermDeposit;
    private FrequencyOptionYearly termUnit;
    private Integer minTermLength;
    private Integer maxTermLength;
    private Integer defaultTermLength;
}
