import { ValueConstraint } from "./ValueConstraint";

export class PenaltySetting {
    sameConstraintForAllCurrency = true;
    /**
     * 1 => No Penalty
     * 2 => Overdue Principal * # of Late Days * Penalty Rate
     * 3 => (Overdue Principal + Overdue Interest) * # of Late Days * Penalty Rate
     * 4 => Outstanding Principal * # of Late Days * Penalty Rate
     */
    calculationMethod: PenaltyCalculationMethod = PenaltyCalculationMethod.NONE;
    /**
     * Penalty Tolerance Period X Days.
     */
    penaltyTolerancePeriod: number | null = 1;
    /**
     * Penalty Rate Constraints (%).
     */
    penaltyRateConstraints: ValueConstraint[] = [];
}

export enum PenaltyCalculationMethod {
    /**
     * No Penalty.
     */
    NONE = "NONE",
    /**
     * Overdue Principal * # of Late Days * Penalty Rate.
     */
    OVERDUE_BALANCE = "OVERDUE_BALANCE",
    /**
     * (Overdue Principal + Overdue Interest) * # of Late Days * Penalty Rate.
     */
    OVERDUE_BALANCE_AND_INTEREST = "OVERDUE_BALANCE_AND_INTEREST",
    /**
     * Outstanding Principal * # of Late Days * Penalty Rate.
     */
    OUTSTANDING_PRINCIPAL = "OUTSTANDING_PRINCIPAL"
}
