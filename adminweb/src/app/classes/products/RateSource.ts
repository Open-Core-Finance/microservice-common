import { GeneralModel, ListableItem } from "../CommonClasses";
import { RateType } from "./Rate";

export class RateSource implements GeneralModel<string>, ListableItem {
    id: string = "";
    name: string = "";
    index: number = 0;
    note: string = "";
    type: RateType = RateType.INTEREST;
}