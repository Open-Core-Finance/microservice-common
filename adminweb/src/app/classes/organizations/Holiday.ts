import { GeneralModel, ListableItem } from "../CommonClasses";

export class Holiday implements GeneralModel<string>, ListableItem {
    id: string = "";
    description: string = "";
    index: number = 0;
    repeatYearly: boolean = false;
    holidayDate: Date = new Date();
}