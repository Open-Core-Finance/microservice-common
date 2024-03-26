import { GeneralModel, ListableItem } from "../CommonClasses";

export class Region implements GeneralModel<number>, ListableItem {
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
}