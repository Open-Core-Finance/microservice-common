import { GeneralModel, ListableItem } from "../CommonClasses";

export class ExchangeRate implements GeneralModel<string>, ListableItem {
    id: string = "";
    name: string = "";
    index: number = 0;
    sellRate: number = 0.0;
    buyRate: number = 0.0;
}