import { CustomerType } from "../customers/CustomerType";
import { CreditArrangementManaged } from "../products/CreditArrangementManaged";
import { DepositLimit } from "../products/DepositLimit";
import { DepositProductFee } from "../products/DepositProduct";
import { FrequencyOptionYearly } from "../products/FrequencyOption";
import { TieredInterestItem } from "../products/TieredInterestItem";
import { CurrencyLimitValue } from "../products/ValueConstraint";
import { WithdrawalLimit } from "../products/WithdrawalLimit";
import { Account, CreateAccountRequest } from "./Account";
import { DepositAccountInterestRate } from "./DepositAccountInterestRate";

export abstract class GenericDepositAccount extends Account {
    
    productFees: DepositProductFee[] = [];
    allowDepositAfterMaturityDate = false;
    /**
     * Interest Rate.
     */
    enableInterestRate: boolean = false;
    interestRate: DepositAccountInterestRate | null = new DepositAccountInterestRate();

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
    overdraftsInterest: DepositAccountInterestRate | null = new DepositAccountInterestRate();
    maxOverdraftLimit: CurrencyLimitValue[] = [];
    overdraftsUnderCreditArrangementManaged: CreditArrangementManaged | null = CreditArrangementManaged.NO;

    enableTermDeposit: boolean = false;
    termUnit: FrequencyOptionYearly | null = FrequencyOptionYearly.DAY;
    termLength: number | null = 0.0;

    customerType = CustomerType.INDIVIDUAL;
    customerId: any = "";

    public override assignDataTo<A extends CreateAccountRequest>(requestObj: A) {
        super.assignDataTo(requestObj);
        if (requestObj instanceof CreateDepositAccountRequest) {
            requestObj.termLength = this.termLength;
            requestObj.customerId = this.customerId;
            requestObj.customerType = this.customerType;
            requestObj.enableTermDeposit = this.enableTermDeposit;
            requestObj.termUnit = this.termUnit;
            if (this.interestRate) {
                requestObj.interestRateValues = this.interestRate.interestRateValues;
            }
        }
    }
}

export class DepositAccount extends GenericDepositAccount {
}

export class CryptoAccount extends GenericDepositAccount {
}

export abstract class GenericCreateDepositAccountRequest extends CreateAccountRequest {
    termLength: number| null = 3;
    customerId:any = 0;
    customerType = CustomerType.INDIVIDUAL;

    interestItems: TieredInterestItem[] = [];
    interestRateValues: CurrencyLimitValue[] = [];
    enableTermDeposit: boolean = false;
    enableInterestRate: boolean = false;
    termUnit: FrequencyOptionYearly | null = FrequencyOptionYearly.DAY;
}

export class CreateDepositAccountRequest extends GenericCreateDepositAccountRequest {
}

export class CreateCryptoAccountRequest extends GenericCreateDepositAccountRequest {
}
