import { ArrearsDaysCalculatedFrom } from "../products/ArrearsSetting";
import { CurrencyLimitValue } from "../products/ValueConstraint";

export class AccountArrearsSetting {

    sameConstraintForAllCurrency = true;
    /**
     * Arrears Tolerance Period in Days.
     */
    tolerancePeriods: CurrencyLimitValue[] = [];

    /**
     * Include/Exclude Non-Working Days in Arrears Tolerance Period and Penalty Calculation Method.
     */
    includeNonWorkingDay = false;

    /**
     * Arrears Days Calculated From.
     */
    daysCalculatedFrom = ArrearsDaysCalculatedFrom.FIRST_INTO_ARREARS;
    /**
     * Arrears Tolerance Amount (% of Outstanding Principal).
     */
    toleranceAmounts: CurrencyLimitValue[] = [];
    /**
     * With a floor (minimum).
     */
    floors: CurrencyLimitValue[] = [];
}