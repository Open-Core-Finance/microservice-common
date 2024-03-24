import { CustomerIdentityType } from "./CustomerIdentityType";

export class Nationality {
    countryCode = "";
    identityType: CustomerIdentityType = CustomerIdentityType.ID_CARD_12D;
    identityNumber = "";
    issuingCountry = "";
    issuingPlace = "";
    issuingDate = new Date();
    expiringDate = new Date();
}