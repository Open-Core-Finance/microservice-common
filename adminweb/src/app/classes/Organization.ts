import { GeneralModel, ListableItem } from "./CommonClasses";
import { Currency } from "./Currency";
import { DayOfWeek } from "./DayOfWeek";

export class Organization implements ListableItem, GeneralModel {
    id: string = "";
    name: string = "";
    index: number = 0;
    streetAddressLine1: string = "";
    city: string = "";
    state: string = "";
    zipPostalCode: string = "";
    country: string = "";
    phoneNumber: string = "";
    email: string = "";
    currency: Currency = new Currency();
    timezone: string = "";
    localDateFormat: string = "yyyy-MM-dd";
    localDateTimeFormat: string = "yyyy-MM-dd hh:mm:ss";
    decimalMark: string = ".";
    logoUrl: string = "";
    iconUrl: string = "";

    nonWorkingDays: DayOfWeek[] = [];

    constructor() {
        this.index = 0;
    }
}