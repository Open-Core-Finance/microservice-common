import { GeneralModel, ListableItem } from "./CommonClasses";

export class Currency implements GeneralModel<string>, ListableItem {
    index: number = 0;
    id: string = "ALL";
    symbol: string = "$";
    decimalMark = ".";
    symbolAtBeginning = true;

    constructor() {
        this.index = 0;
    }
}