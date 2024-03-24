import { GeneralModel, ListableItem } from "../CommonClasses";
import { UserDto } from "../User";

export class Customer implements GeneralModel<number>, ListableItem {
    id: number = 0;
    index: number = 0;

    lastModifiedDate: Date = new Date();
    createdDate: Date = new Date();
    createdBy: UserDto = new UserDto();
    lastModifiedBy: UserDto = new UserDto();


    contactPhone = "";
    contactEmail = "";
    contactCompanyPhone = "";
    mailingStreetAddressLine1 = "";
    mailingStreetAddressLine2 = "";
    mailingDistrict = "";
    mailingCity = "";
    mailingState = "";
    mailingZipPostalCode = "";
    mailingCountry = "";
    streetAddressLine1 = "";
    streetAddressLine2 = "";
    district = "";
    city = "";
    state = "";
    zipPostalCode = "";
    country = "";

    consentMarketing = false;
    consentNonMarketing = false;
    consentAbroad = false;
    consentTransferToThirdParty = false;
}