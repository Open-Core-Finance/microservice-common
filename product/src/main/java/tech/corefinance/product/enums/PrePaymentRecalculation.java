package tech.corefinance.product.enums;

public enum PrePaymentRecalculation {
    /**
     * No Recalculation.
     */
    NO_RECALCULATION,
    /**
     * Reschedule Remaining Repayments.
     */
    RESCHEDULE_REMAINING_REPAYMENTS,
    /**
     * Recalculate the Schedule, Keep the Same Number of Terms.
     */
    RECALCULATE_SCHEDULE_KEEP_SAME_NUMBER_OF_TERMS,
    /**
     * Recalculate the Schedule, Keep the Same Principal Amount.
     */
    RECALCULATE_SCHEDULE_KEEP_SAME_PRINCIPAL_AMOUNT;
}
