import { GeneralModel, ListableItem } from "../CommonClasses";
import {User} from "../User";
import { AccountState } from "../accounts/AccountState";
import { AccountFee } from "./AccountFee";

export abstract class Account implements GeneralModel<string>, ListableItem {
    id: string = "";
    name: string = "";
    index: number = 0;

    createdDate = new Date();
    createdBy = new User();
    lastModifiedDate =  new Date();
    lastModifiedBy = new User();

    category = "";
    type = "";
    description = "";
    status: AccountState = AccountState.NEW;

    accountFees: AccountFee = new AccountFee();

    supportedCurrencies: string[] = [];

    productId = "";
}