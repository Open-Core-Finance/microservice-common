import { MonthlyPayOption } from "../products/Product";
import { ProductFeeType } from "../products/ProductFeeType";

export class AccountFee {
    activated = true;
    id = "";
    name = "";
    type: ProductFeeType = ProductFeeType.MONTHLY_FEE;
    amount = "";
    currencyId = "";
    disbursedPercent = 0.0;

    monthlyPayOption: MonthlyPayOption = MonthlyPayOption.MONTHLY_LAST_DAY;
    requiredFeeApplication = false;
}