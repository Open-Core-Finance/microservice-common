import { PenaltyCalculationMethod } from "../products/PenaltySetting";
import { CurrencyLimitValue } from "../products/ValueConstraint";

export class AccountPenaltySetting {
    sameConstraintForAllCurrency = true;

    calculationMethod: PenaltyCalculationMethod = PenaltyCalculationMethod.OVERDUE_BALANCE;
    /**
     * Penalty Tolerance Period X Days.
     */
    penaltyTolerancePeriod = 0;
    /**
     * Penalty Rate Constraints (%).
     */
    penaltyRateValues: CurrencyLimitValue[] = [];
}