import { FrequencyOption } from "../products/FrequencyOption";
import { GracePeriodType } from "../products/Repayment";
import { RepaymentCurrencyRounding } from "../products/Repayment";
import { RepaymentsScheduleEditing } from "../products/Repayment";
import { NonWorkingDaysRescheduling } from "../products/Repayment";
import { RepaymentScheduleRounding } from "../products/Repayment";
import { ShortMonthHandling } from "../products/Repayment";
import { RepaymentSchedulingMethod } from "../products/Repayment";
import { CurrencyLimitValue } from "../products/ValueConstraint";

export class AccountRepaymentScheduling {

    repaymentMethod: RepaymentSchedulingMethod = RepaymentSchedulingMethod.INTERVAL;
    /**
     * Interval repayment scheduling value.
     */
    intervalValue = 0;
    /**
     * Interval repayment scheduling option.
     */
    intervalOption: FrequencyOption | null = FrequencyOption.MONTH;
    /**
     * Fixed days repayment scheduling value.
     */
    repaymentDays: number[] = [];

    shortMonthHandling: ShortMonthHandling | null = ShortMonthHandling.LAST_DAY_OF_MONTH;
    installmentsValues: CurrencyLimitValue[] = [];
    /**
     * Automatically add a default offset in days to the first installment due date and specify
     * the minimum and maximum days that can be added to the first installment date.
     */
    firstDueDateOffsetValues: CurrencyLimitValue[] = [];
    /**
     * Collect Principal Every X Repayments.
     */
    collectPrincipalEveryRepayments = 0;

    gracePeriodType: GracePeriodType = GracePeriodType.NO;
    /**
     * Principal Grace Period. If this option is not null then Pure Grace Period must be null.
     */
    gracePeriodValues: CurrencyLimitValue[] = [];

    scheduleRounding: RepaymentScheduleRounding = RepaymentScheduleRounding.ROUND_PRINCIPAL_AND_INTEREST_REMAINDER_INTO_LAST_REPAYMENT;
    currencyRounding: RepaymentCurrencyRounding = RepaymentCurrencyRounding.ROUND_TO_NEAREST;

    nonWorkingDaysRescheduling: NonWorkingDaysRescheduling = NonWorkingDaysRescheduling.NO_RESCHEDULING;

    repaymentsScheduleEditing: RepaymentsScheduleEditing = new RepaymentsScheduleEditing();

    sameConstraintForAllCurrency = true;
}