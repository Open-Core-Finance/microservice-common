package tech.corefinance.product.enums;

public enum ProductFeeType {
    /**
     * Manual for deposit or loan account.
     */
    MANUAL_FEE,
    /**
     * Deposit monthly fee.
     */
    MONTHLY_FEE,
    /**
     * Planned (Applied on Due Dates) for loan.
     */
    PLANNED,
    /**
     * Deducted Disbursement.
     */
    DEDUCTED_DISBURSEMENT,
    /**
     * Capitalized Disbursement.
     */
    CAPITALIZED_DISBURSEMENT,
    /**
     * Upfront Disbursement.
     */
    UPFRONT_DISBURSEMENT,
    /**
     * Late Repayment.
     */
    LATE_REPAYMENT,
    /**
     * Payment Due (Applied Upfront).
     */
    PAYMENT_DUE_UPFRONT,
    /**
     * Payment Due (Applied on Due Dates).
     */
    PAYMENT_DUE_DUE_DATE;
}
