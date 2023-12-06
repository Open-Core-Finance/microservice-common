import { GeneralModel, ListableItem } from "./CommonClasses";

export class Currency implements GeneralModel, ListableItem {
    index: number = 0;
    id: string = "";
    name: string = "";
    symbol: string = "";
    decimalMark = ".";
    symbolAtBeginning = false;

    constructor() {
        this.index = 0;
    }
}