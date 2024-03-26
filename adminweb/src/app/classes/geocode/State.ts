import { GeneralModel, ListableItem } from "../CommonClasses";

export class State implements GeneralModel<number>, ListableItem {
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

    countryCode = "";
    countryId = 0;
    fipsCode = "";

    iso2 = "";
    type = "";
    latitude = 0.0;
    longitude = 0.0;
}