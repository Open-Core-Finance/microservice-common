import { GeneralModel, ListableItem } from "../CommonClasses";
import { UserDto} from "../User";
import { AccountState } from "../accounts/AccountState";

export abstract class Account implements GeneralModel<string>, ListableItem {
    id: string = "";
    name: string = "";
    index: number = 0;

    createdDate = new Date();
    createdBy = new UserDto();
    lastModifiedDate =  new Date();
    lastModifiedBy = new UserDto();

    category = "";
    type = "";
    description = "";
    status: AccountState = AccountState.NEW;

    supportedCurrencies: string[] = [];

    productId = "";
}