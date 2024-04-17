package tech.corefinance.account.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.*;
import tech.corefinance.product.common.model.CurrencyValue;
import tech.corefinance.product.common.model.RepaymentsScheduleEditing;
import tech.corefinance.product.common.model.ValueConstraint;

import java.util.List;

@Data
public class AccountRepaymentScheduling {
    private RepaymentSchedulingMethod repaymentMethod;
    /**
     * Interval repayment scheduling value.
     */
    private Integer intervalValue;
    /**
     * Interval repayment scheduling option.
     */
    private FrequencyOption intervalOption;
    /**
     * Fixed days repayment scheduling value.
     */
    private List<Integer> repaymentDays;

    private ShortMonthHandling shortMonthHandling;
    private List<CurrencyValue> installmentsValues;
    /**
     * Automatically add a default offset in days to the first installment due date and specify
     * the minimum and maximum days that can be added to the first installment date.
     */
    private List<CurrencyValue> firstDueDateOffsetValues;
    /**
     * Collect Principal Every X Repayments.
     */
    private Integer collectPrincipalEveryRepayments;

    private GracePeriodType gracePeriodType;
    /**
     * Principal Grace Period. If this option is not null then Pure Grace Period must be null.
     */
    private List<CurrencyValue> gracePeriodValues;

    private RepaymentScheduleRounding scheduleRounding;
    private RepaymentCurrencyRounding currencyRounding;

    private NonWorkingDaysRescheduling nonWorkingDaysRescheduling;

    private RepaymentsScheduleEditing repaymentsScheduleEditing;

    private boolean sameConstraintForAllCurrency;
}
