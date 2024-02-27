package tech.corefinance.product.common.enums;

public enum PenaltyCalculationMethod {
    /**
     * No Penalty.
     */
    NONE,
    /**
     * Overdue Principal * # of Late Days * Penalty Rate.
     */
    OVERDUE_BALANCE,
    /**
     * (Overdue Principal + Overdue Interest) * # of Late Days * Penalty Rate.
     */
    OVERDUE_BALANCE_AND_INTEREST,
    /**
     * Outstanding Principal * # of Late Days * Penalty Rate.
     */
    OUTSTANDING_PRINCIPAL
}
