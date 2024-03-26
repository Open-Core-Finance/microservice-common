import { GeneralModel, ListableItem } from "../CommonClasses";

export class Country implements GeneralModel<number>, ListableItem {
    index = 0;
    id = 0;
    name = "";
    translations = "";
    enabled = true;
    /**
     * Rapid API GeoDB Cities.
     */
    wikiDataId = "";
    createdDate = new Date();
    lastModifiedDate = new Date();
    
    iso3 = "";
    iso2 = "";
    numericCode = "";
    phoneCode = "";
    capital = "";
    currency = "";
    currencyName = "";
    currencySymbol = "";
    tld = "";
    nativePeople = "";

    regionName = "";
    regionId = 0;

    subregionName = "";
    subRegionId = 0;

    nationality = "";
    timezones = "";
    latitude = 0.0;
    longitude = 0.0;
    emoji = "";
    emojiu = "";
}