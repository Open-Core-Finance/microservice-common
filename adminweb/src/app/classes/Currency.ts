import { GeneralModel, ListableItem } from "./CommonClasses";

export class Currency implements GeneralModel<string>, ListableItem {
    index: number = 0;
    id: string = "";
    symbol: string = "";
    decimalMark = ".";
    symbolAtBeginning = false;

    constructor() {
        this.index = 0;
    }
}