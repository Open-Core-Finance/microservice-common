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

    categoryId = "";
    categoryName = "";
    typeId = "";
    typeName = "";
    description = "";
    status: AccountState = AccountState.NEW;

    supportedCurrencies: string[] = [];

    productId = "";

    public assignDataTo<A extends CreateAccountRequest>(requestObj: A) {
        requestObj.id = this.id;
        requestObj.name = this.name;
        requestObj.categoryId = this.categoryId;
        requestObj.typeId = this.typeId;
        requestObj.description = this.description;
        requestObj.supportedCurrencies = this.supportedCurrencies;
        requestObj.productId = this.productId;
    }
}

export abstract class CreateAccountRequest implements GeneralModel<string> {
    id = "";
    name = "";
    categoryId = "";
    typeId = "";
    description = "";
    supportedCurrencies: string[] = [];
    productId = "";
}