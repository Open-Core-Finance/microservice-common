import { FrequencyOption } from "./FrequencyOption";
import { ValueConstraint } from "./ValueConstraint";

export class RepaymentCollection {
    /**
     * Horizontal or Vertical.
     */
    repaymentHorizontal: boolean = true;
    acceptPrePayments = true;
    /**
     * Accept Pre-Payments. Whether interest that's not yet applied in the account can be paid in advance.
     */
    autoApplyInterestPrePayments: boolean  = true;
    /**
     * Dynamic term loan Pre-Payment Recalculation.
     */
    prePaymentRecalculation: PrePaymentRecalculation = PrePaymentRecalculation.RECALCULATE_SCHEDULE_KEEP_SAME_NUMBER_OF_TERMS;

    acceptPrepaymentFutureInterest = false;

    /**
     *  Arrange repayment types according to which should be paid first and last on partial or over-payments.
     */
    repaymentTypesOrder: RepaymentType[] = [
        RepaymentType.FEE, RepaymentType.PENALTY, RepaymentType.INTEREST, RepaymentType.PRINCIPAL
    ];
}

export enum PrePaymentRecalculation {
    /**
     * No Recalculation.
     */
    NO_RECALCULATION = "NO_RECALCULATION",
    /**
     * Reschedule Remaining Repayments.
     */
    RESCHEDULE_REMAINING_REPAYMENTS = "RESCHEDULE_REMAINING_REPAYMENTS",
    /**
     * Recalculate the Schedule, Keep the Same Number of Terms.
     */
    RECALCULATE_SCHEDULE_KEEP_SAME_NUMBER_OF_TERMS = "RECALCULATE_SCHEDULE_KEEP_SAME_NUMBER_OF_TERMS",
    /**
     * Recalculate the Schedule, Keep the Same Principal Amount.
     */
    RECALCULATE_SCHEDULE_KEEP_SAME_PRINCIPAL_AMOUNT = "RECALCULATE_SCHEDULE_KEEP_SAME_PRINCIPAL_AMOUNT"
}

export enum RepaymentType {
    FEE = "FEE", PENALTY = "PENALTY", INTEREST = "INTEREST", PRINCIPAL = "PRINCIPAL"
}

export class RepaymentScheduling {
    repaymentMethod = RepaymentSchedulingMethod.INTERVAL;
    /**
     * Interval repayment scheduling value.
     */
    intervalValue: number | null = 1;
    /**
     * Interval repayment scheduling option.
     */
    intervalOption: FrequencyOption | null = FrequencyOption.MONTH;
    /**
     * Fixed days repayment scheduling value.
     */
    repaymentDays: number[] = [];

    shortMonthHandling: ShortMonthHandling | null = ShortMonthHandling.LAST_DAY_OF_MONTH;
    installmentsConstraints: ValueConstraint[] = [];
    /**
     * Automatically add a default offset in days to the first installment due date and specify
     * the minimum and maximum days that can be added to the first installment date.
     */
    firstDueDateOffsetConstraints: ValueConstraint[] = [];
    /**
     * Collect Principal Every X Repayments.
     */
    collectPrincipalEveryRepayments: number | null = 1;

    gracePeriodType: GracePeriodType = GracePeriodType.NO;
    /**
     * Principal Grace Period. If this option is not null then Pure Grace Period must be null.
     */
    gracePeriodsConstraints: ValueConstraint[] = [];

    scheduleRounding: RepaymentScheduleRounding = RepaymentScheduleRounding.NO_ROUNDING;
    currencyRounding = RepaymentCurrencyRounding.NO_ROUNDING;

    nonWorkingDaysRescheduling: NonWorkingDaysRescheduling = NonWorkingDaysRescheduling.NO_RESCHEDULING;

    repaymentsScheduleEditing: RepaymentsScheduleEditing = new RepaymentsScheduleEditing();

    sameConstraintForAllCurrency = true;
}

export enum RepaymentSchedulingMethod {
    INTERVAL = "INTERVAL", FIXED_DAY_OF_MONTH = "FIXED_DAY_OF_MONTH"
}

export enum ShortMonthHandling {
    LAST_DAY_OF_MONTH = "LAST_DAY_OF_MONTH", FIRST_DAY_NEXT_MONTH = "FIRST_DAY_NEXT_MONTH"
}

export enum RepaymentScheduleRounding {
    NO_ROUNDING = "NO_ROUNDING",
    ROUND_REMAINDER_INTO_LAST_REPAYMENT = "ROUND_REMAINDER_INTO_LAST_REPAYMENT",
    ROUND_PRINCIPAL_AND_INTEREST_REMAINDER_INTO_LAST_REPAYMENT = "ROUND_PRINCIPAL_AND_INTEREST_REMAINDER_INTO_LAST_REPAYMENT"
}

export enum RepaymentCurrencyRounding {
    NO_ROUNDING = "NO_ROUNDING", ROUND_TO_NEAREST = "ROUND_TO_NEAREST", ROUND_UP_TO_NEAREST = "ROUND_UP_TO_NEAREST"
}

export enum NonWorkingDaysRescheduling {
    NO_RESCHEDULING = "NO_RESCHEDULING", MOVE_NEXT_WORKING_DAY = "MOVE_NEXT_WORKING_DAY",
    MOVE_BACKWARD_WORKING_DAY = "MOVE_BACKWARD_WORKING_DAY", EXTEND_SCHEDULE = "EXTEND_SCHEDULE"
}

export class RepaymentsScheduleEditing {
    // All
    adjustPaymentDates: boolean = false;
    // All
    adjustPrincipalPaymentSchedule: boolean = false;
    // Dynamic term loan
    adjustNumberInstallments: boolean = false;
    // Dynamic term loan & Fixed term loan
    configurePaymentHolidays: boolean = false;
    // Not Dynamic term loan
    adjustFeePaymentSchedule = false;
    // Not Dynamic term loan
    adjustPenaltyPaymentSchedule = false;
    // Fixed term loan
    adjustInterestPaymentSchedule = false;
}

export enum GracePeriodType {
    NO = "NO", PRINCIPAL = "PRINCIPAL", PURE = "PURE"
}