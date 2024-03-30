import { Gender } from "../Gender";
import { Customer } from "./Customer";
import { MaritalStatus } from "./MaritalStatus";
import { Nationality } from "./Nationality";

export class IndividualCustomer extends Customer {

    contactHomePhone = "";
    title = "Mr";
    firstName = "";
    middleName = "";
    lastName = "";
    gender: Gender = Gender.UNKNOWN;
    cisNumber = "";
    placeOfBirth = "";
    dob = new Date();

    maritalStatus: MaritalStatus = MaritalStatus.SINGLE;
    nationality: Nationality = new Nationality();
    secondNationality: Nationality = new Nationality();

    get name(): string {
        return this.firstName;
    }
}