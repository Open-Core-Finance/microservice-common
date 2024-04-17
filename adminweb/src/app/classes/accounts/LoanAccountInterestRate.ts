import { InterestCalculationMethod } from "../products/InterestCalculationMethod";
import { InterestDayInYear } from "../products/InterestDayInYear";
import { RepaymentsInterestCalculation } from "../products/LoanProduct";
import { AccruedInterestPostingFrequency, LoanInterestCalculationMethod, LoanInterestType } from "../products/LoanProduct";
import { CurrencyLimitValue } from "../products/ValueConstraint";

export class LoanAccountInterestRate {
    interestCalculationMethod: LoanInterestCalculationMethod = LoanInterestCalculationMethod.DECLINING_BALANCE;
    accruedInterestPostingFrequency: AccruedInterestPostingFrequency = AccruedInterestPostingFrequency.REPAYMENT;
    interestCalculationPoint: InterestCalculationMethod | null = InterestCalculationMethod.PERCENTAGE_PER_MONTH;
    percentPerDay = 0;
    interestType: LoanInterestType = LoanInterestType.CAPITALIZED;
    interestDayInYear: InterestDayInYear = InterestDayInYear.FIXED_365_DAYS;
    /**
     * Interest Rate Constraints (%) for fixed interest rate. <br/>
     * Interest Spread Constraints (%) for index rate source.
     */
    interestRateValues: CurrencyLimitValue[] = [];
    sameConstraintForAllCurrency = false;
    interestRateIndexSource: string = "";
    repaymentsInterestCalculation: RepaymentsInterestCalculation = RepaymentsInterestCalculation.ACTUAL_NUMBER_OF_DAYS;
}