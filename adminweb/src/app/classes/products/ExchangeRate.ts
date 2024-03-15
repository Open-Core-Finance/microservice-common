import { ListableItem } from "../CommonClasses";
import { User } from "../User";

export class ExchangeRate implements ListableItem {
    fromCurrency = "";
    toCurrency = "";
    margin = 0.0;
    index: number = 0;
    sellRate: number = 0.0;
    buyRate: number = 0.0;

    lastModifiedDate: Date = new Date();
    createdDate: Date = new Date();
    createdBy: User = new User();
    lastModifiedBy: User = new User();
}
