package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.FrequencyOption;
import tech.corefinance.product.enums.NonWorkingDaysRescheduling;
import tech.corefinance.product.enums.RepaymentScheduleRounding;
import tech.corefinance.product.enums.ShortMonthHandling;

import java.util.List;

@Data
public class RepaymentScheduling {
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
    private ValueConstraint installmentsConstraint;
    /**
     * Automatically add a default offset in days to the first installment due date and specify
     * the minimum and maximum days that can be added to the first installment date.
     */
    private ValueConstraint firstDueDateOffsetConstraint;
    /**
     * Collect Principal Every X Repayments.
     */
    private Integer collectPrincipalEveryRepayments;
    /**
     * Principal Grace Period. If this option is not null then Pure Grace Period must be null.
     */
    private ValueConstraint principalGracePeriod;
    /**
     * Pure Grace Period. If this option is not null then Principal Grace Period must be null.
     */
    private ValueConstraint pureGracePeriod;

    private RepaymentScheduleRounding scheduleRounding;

    private NonWorkingDaysRescheduling nonWorkingDaysRescheduling;

    private RepaymentsScheduleEditing repaymentsScheduleEditing;
}
