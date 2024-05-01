import { GeneralModel, ListableItem } from "../CommonClasses";
import { UserDto } from "../User";
import { AccountType } from "../accounts/AccountType";
import { CustomerType } from "../customers/CustomerType";

export class InternalFundTransfer implements GeneralModel<string>, ListableItem {
    id: string = "";
    index: number = 0;

    lastModifiedDate: Date = new Date();
    createdDate: Date = new Date();
    createdBy: UserDto = new UserDto();
    lastModifiedBy: UserDto = new UserDto();

    fromCustomerType: CustomerType = CustomerType.INDIVIDUAL;
    fromCustomerId: any = "";
    fromAccountId = "";
    fromAccountType: AccountType = AccountType.DEPOSIT;
    fromCurrency = "";
    fromAmount = "";

    toCustomerType: CustomerType = CustomerType.INDIVIDUAL;
    toCustomerId: any = "";
    toAccountId = "";
    toAccountType: AccountType = AccountType.DEPOSIT;
    toCurrency = "";
    toAmount = "";
}