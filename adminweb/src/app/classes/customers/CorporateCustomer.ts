import { Customer } from "./Customer";

export class CorporateCustomer extends Customer {
    name: string = "";
    taxNumber = "";
    startDate = new Date();
}