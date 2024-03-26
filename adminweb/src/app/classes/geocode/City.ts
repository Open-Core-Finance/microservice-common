import { GeneralModel, ListableItem } from "../CommonClasses";

export class City implements GeneralModel<number>, ListableItem {
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

    stateId = 0;
    stateCode = "";

    countryCode = "";
    countryId = 0;

    latitude = 0.0;
    longitude = 0.0;
}