package tech.corefinance.account.common.model;

public enum MonthlyPayOption {
    /**
     * Pay monthly base on activation date.
     */
    MONTHLY_FROM_ACTIVATION,
    /**
     * Pay first day every month.
     */
    MONTHLY_FIRST_DAY,
    /**
     * Pay last day every month.
     */
    MONTHLY_LAST_DAY;
}
