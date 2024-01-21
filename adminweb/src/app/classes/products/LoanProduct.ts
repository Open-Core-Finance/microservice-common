import { ArrearsSetting } from "./ArrearsSetting";
import { CreditArrangementManaged } from "./CreditArrangementManaged";
import { InterestCalculationMethod } from "./InterestCalculationMethod";
import { InterestDayInYear } from "./InterestDayInYear";
import { InterestRate } from "./InterestRate";
import { PenaltySetting } from "./PenaltySetting";
import { Product } from "./Product";
import { RepaymentCollection, RepaymentScheduling } from "./Repayment";
import { ValueConstraint } from "./ValueConstraint";

export class LoanProduct extends Product {

    loanValues: ValueConstraint[] = [];

    underCreditArrangementManaged: CreditArrangementManaged | null = CreditArrangementManaged.OPTIONAL;

    interestRate: LoanInterestRate | null = new LoanInterestRate();
    repaymentScheduling: RepaymentScheduling | null = new RepaymentScheduling();
    repaymentCollection: RepaymentCollection | null = new RepaymentCollection();
    arrearsSetting: ArrearsSetting | null = new ArrearsSetting();
    penaltySetting: PenaltySetting | null = new PenaltySetting();

    closeDormantAccounts: boolean = false;
    lockArrearsAccounts: boolean = false;
    capCharges: boolean = false;

    enableGuarantors = false;
    enableCollateral = false;
    percentSecurityPerLoan: number = 0.0;
}

export class LoanInterestRate implements InterestRate {
    interestCalculationMethod: LoanInterestCalculationMethod = LoanInterestCalculationMethod.FLAT;
    accruedInterestPostingFrequency: AccruedInterestPostingFrequency = AccruedInterestPostingFrequency.REPAYMENT;
    interestType: LoanInterestType = LoanInterestType.CAPITALIZED;
    interestCalculationPoint: InterestCalculationMethod | null = InterestCalculationMethod.PERCENTAGE_PER_MONTH;
    percentPerDay = 0;
    interestDayInYear: InterestDayInYear = InterestDayInYear.FIXED_365_DAYS;
    /**
     * Interest Rate Constraints (%) for fixed interest rate. <br/>
     * Interest Spread Constraints (%) for index rate source.
     */
    interestRateConstraints: ValueConstraint[] = [];
    interestRateIndexSource = "";
    repaymentsInterestCalculation: RepaymentsInterestCalculation | null = RepaymentsInterestCalculation.ACTUAL_NUMBER_OF_DAYS;
}

export enum LoanInterestCalculationMethod {
    FLAT = "FLAT", DECLINING_BALANCE = "DECLINING_BALANCE", DECLINING_BALANCE_DISCOUNTED = "DECLINING_BALANCE_DISCOUNTED"
}

export enum AccruedInterestPostingFrequency {
    /**
     * Accrued Interest Posting Frequency on Disbursement.
     */
    DISBURSEMENT = "DISBURSEMENT",
    /**
     * Accrued Interest Posting Frequency on Repayment.
     */
    REPAYMENT = "REPAYMENT"
}

export enum LoanInterestType {
    /**
     * Simple Interest.
     */
    SIMPLE = "SIMPLE",
    /**
     * Compound Interest with Compounding Frequency as Daily;
     */
    DAILY_COMPOUND = "DAILY_COMPOUND",
    /**
     * Capitalized Interest.
     */
    CAPITALIZED = "CAPITALIZED"
}

export enum RepaymentsInterestCalculation {

    /**
     * Using Actual Number of Days.
     */
    ACTUAL_NUMBER_OF_DAYS = "ACTUAL_NUMBER_OF_DAYS",
    /**
     * Using Repayment Periodicity.
     */
    REPAYMENT_PERIODICITY = "REPAYMENT_PERIODICITY"
}
