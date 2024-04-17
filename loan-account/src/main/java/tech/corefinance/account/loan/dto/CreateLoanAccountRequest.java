package tech.corefinance.account.loan.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.account.common.dto.CreateAccountRequest;
import tech.corefinance.account.common.enums.CustomerType;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.product.common.model.CurrencyValue;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateLoanAccountRequest extends CreateAccountRequest implements CreateUpdateDto<String> {
    private long customerId;
    private CustomerType customerType;

    private List<CurrencyValue> loanAppliedValues;

    private List<CurrencyValue> interestRateValues;

    /**
     * Repayment scheduling settings.
     */
    private List<CurrencyValue> installmentsValues;
    /**
     * Automatically add a default offset in days to the first installment due date and specify
     * the minimum and maximum days that can be added to the first installment date.
     */
    private List<CurrencyValue> firstDueDateOffsetValues;
    /**
     * Principal Grace Period. If this option is not null then Pure Grace Period must be null.
     */
    private List<CurrencyValue> gracePeriodValues;

    /**
     * Arrears Tolerance Period in Days.
     */
    private List<CurrencyValue> tolerancePeriods;
    /**
     * Arrears Tolerance Amount (% of Outstanding Principal).
     */
    private List<CurrencyValue> toleranceAmounts;

    /**
     * Penalty Rate Constraints (%).
     */
    private List<CurrencyValue> penaltyRateValues;
}
