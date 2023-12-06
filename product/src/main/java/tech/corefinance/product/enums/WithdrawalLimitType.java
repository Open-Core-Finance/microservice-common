package tech.corefinance.product.enums;

public enum WithdrawalLimitType {
    /**
     * Yearly limit.
     */
    WITHDRAW_LIMIT_YEARLY,
    /**
     * Monthly limit.
     */
    WITHDRAW_LIMIT_MONTHLY,
    /**
     * Daily limit.
     */
    WITHDRAW_LIMIT_DAILY,
    /**
     * One time withdrawal limit.
     */
    WITHDRAW_LIMIT_EACH_TIME,
    /**
     * Max Withdrawal Amount As a % of total balance on the deposit account.
     */
    WITHDRAWAL_LIMIT_PERCENTAGE_OF_BALANCE;
}
