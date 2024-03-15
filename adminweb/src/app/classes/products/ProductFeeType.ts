export enum ProductFeeType {
    /**
     * Manual for deposit or loan account.
     */
    MANUAL_FEE = "MANUAL_FEE",
    /**
     * Deposit monthly fee.
     */
    MONTHLY_FEE = "MONTHLY_FEE",
    /**
     * Planned (Applied on Due Dates) for loan.
     */
    PLANNED = "PLANNED",
    /**
     * Deducted Disbursement.
     */
    DEDUCTED_DISBURSEMENT = "DEDUCTED_DISBURSEMENT",
    /**
     * Capitalized Disbursement.
     */
    CAPITALIZED_DISBURSEMENT = "CAPITALIZED_DISBURSEMENT",
    /**
     * Upfront Disbursement.
     */
    UPFRONT_DISBURSEMENT = "UPFRONT_DISBURSEMENT",
    /**
     * Late Repayment.
     */
    LATE_REPAYMENT = "LATE_REPAYMENT",
    /**
     * Payment Due (Applied Upfront).
     */
    PAYMENT_DUE_UPFRONT = "PAYMENT_DUE_UPFRONT",
    /**
     * Payment Due (Applied on Due Dates).
     */
    PAYMENT_DUE_DUE_DATE = "PAYMENT_DUE_DUE_DATE"
}