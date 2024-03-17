import { CreditArrangementManaged } from "./CreditArrangementManaged";
import { DepositLimit } from "./DepositLimit";
import { FrequencyOptionYearly } from "./FrequencyOption";
import { InterestCalculationMethod } from "./InterestCalculationMethod";
import { InterestDayInYear } from "./InterestDayInYear";
import { InterestRate } from "./InterestRate";
import { MonthlyPayOption, Product } from "./Product";
import { DepositProductFeeType } from "./ProductFeeType";
import { TieredInterestItem } from "./TieredInterestItem";
import { CurrencyLimitValue, ValueConstraint } from "./ValueConstraint";
import { WithdrawalLimit } from "./WithdrawalLimit";

export class DepositProduct extends Product {

    productFees: DepositProductFee[] = [];

    /**
     * Interest Rate.
     */
    enableInterestRate: boolean = false;
    interestRate: DepositInterestRate | null = new DepositInterestRate();

    // Internal control
    autoSetAsDormant: boolean = false;
    daysToSetToDormant: number | null = 0;

    /**
     * Deposit transaction limits.
     */
    depositLimits: DepositLimit[] = [];
    /**
     * Withdrawal Limits.
     */
    withdrawalLimits: WithdrawalLimit[] = [];
    /**
     * Early Closure Period.
     */
    enableEarlyClosurePeriod: boolean = false;
    earlyClosurePeriod: number | null = 0;

    allowOverdrafts: boolean = false;
    overdraftsInterest: DepositInterestRate | null = new DepositInterestRate();
    maxOverdraftLimit: CurrencyLimitValue[] = [];
    overdraftsUnderCreditArrangementManaged: CreditArrangementManaged | null = CreditArrangementManaged.NO;

    enableTermDeposit: boolean = false;
    termUnit: FrequencyOptionYearly | null = FrequencyOptionYearly.DAY;
    minTermLength: number | null = 0.0;
    maxTermLength: number | null = 0.0;
    defaultTermLength: number | null = 0.0;

    allowDepositAfterMaturityDate = false;
}

export class DepositInterestRate implements InterestRate {
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
    interestRateConstraints: ValueConstraint[] = [];
    sameConstraintForAllCurrency = true;
    interestRateIndexSource: string = "";
    // Tiered interest rate
    interestItems: TieredInterestItem[] = [];
}

export enum DepositBalanceInterestCalculation {
    /**
     * Minimum Daily Balance: <br/>
     * System will base the interest calculation on the minimum balance the client had in their account during the day.
     */
    DAILY_MIN = "DAILY_MIN",
    /**
     * Average Daily Balance: <br/>
     * System will calculate the average balance that the client had in their account during the day, and base the interest calculation on that amount.
     */
    DAILY_AVG = "DAILY_AVG",
    /**
     * End of Day Balance: <br/>
     * System will base the interest calculation on the balance the client had in their account at the end of the day.
     */
    END_OF_dAY = "END_OF_dAY"
}

export enum InterestCalculationDateOptionType {
    FIRST_DAY_EVERY_MOTNH = "FIRST_DAY_EVERY_MOTNH",
    EVERY_DAY = "EVERY_DAY",
    EVERY_WEEK = "EVERY_WEEK",
    EVERY_OTHER_WEEK = "EVERY_OTHER_WEEK",
    EVERY_MONTH = "EVERY_MONTH",
    EVERY_THREE_MONTHS = "EVERY_THREE_MONTHS",
    EVERY_SIX_MONTHS = "EVERY_SIX_MONTHS",
    EVERY_YEAR = "EVERY_YEAR",
    /**
     * For example fix every 1st January.
     */
    ON_FIXED_DATE = "ON_FIXED_DATE"
}

export enum DepositInterestRateTerms {
    FIXED = "FIXED", TIERED_PER_BALANCE = "TIERED_PER_BALANCE", TIERED_PER_PERIOD = "TIERED_PER_PERIOD"
}

export class DepositProductFee {
    activated = true;
    id = "";
    name = "";
    type: DepositProductFeeType = DepositProductFeeType.MONTHLY_FEE;
    amount: number | null = 0.0;
    currencyId: string = "";

    monthlyPayOption: MonthlyPayOption = MonthlyPayOption.MONTHLY_FROM_ACTIVATION;
}