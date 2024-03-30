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
    mailingCityId = 0;
    mailingStateId = 0;
    mailingZipPostalCode = "";
    mailingCountry = "";
    mailingCountryId = 0;
    streetAddressLine1 = "";
    streetAddressLine2 = "";
    district = "";
    city = "";
    cityId = 0;
    state = "";
    stateId = 0;
    zipPostalCode = "";
    country = "";
    countryId = 0;

    consentMarketing = false;
    consentNonMarketing = false;
    consentAbroad = false;
    consentTransferToThirdParty = false;
}