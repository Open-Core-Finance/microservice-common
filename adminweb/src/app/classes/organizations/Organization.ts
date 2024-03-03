import { GeneralModel, ListableItem } from "../CommonClasses";

export class Organization implements ListableItem, GeneralModel<string> {
    id: string = "";
    name: string = "";
    index: number = 0;

    constructor() {
        this.index = 0;
    }
}