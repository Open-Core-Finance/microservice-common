import { DepositBalanceInterestCalculation, DepositInterestRateTerms, InterestCalculationDateOptionType } from "../products/DepositProduct";
import { InterestCalculationMethod } from "../products/InterestCalculationMethod";
import { InterestDayInYear } from "../products/InterestDayInYear";
import { TieredInterestItem } from "../products/TieredInterestItem";
import { CurrencyLimitValue } from "../products/ValueConstraint";

export class DepositAccountInterestRate {
    interestRateTerms = DepositInterestRateTerms.FIXED;
    interestCalculationMethod: InterestCalculationMethod | null = InterestCalculationMethod.PERCENTAGE_PER_MONTH;
    percentPerDay = 0;
    balanceInterestCalculation: DepositBalanceInterestCalculation | null = DepositBalanceInterestCalculation.END_OF_dAY;
    calculationDateType: InterestCalculationDateOptionType = InterestCalculationDateOptionType.FIRST_DAY_EVERY_MOTNH;
    calculationDateFixedMonth = new Date().getMonth() + 1;
    calculationDateFixedDay = new Date().getDate();
    interestDayInYear: InterestDayInYear | null = InterestDayInYear.FIXED_365_DAYS;

    applyWithholdingTaxes: boolean | null = false;
    // Fixed interest rate
    allowNegativeInterestRate: boolean | null = false;
    /**
     * Interest Rate Constraints (%) for fixed interest rate. <br/>
     * Interest Spread Constraints (%) for index rate source.
     */
    interestRateValues: CurrencyLimitValue[] = [];
    sameInterestForAllCurrency = true;
    interestRateIndexSource: string = "";
    // Tiered interest rate
    interestItems: TieredInterestItem[] = [];
}