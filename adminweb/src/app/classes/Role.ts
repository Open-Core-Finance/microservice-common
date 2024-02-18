import { GeneralModel, ListableItem } from "./CommonClasses";
import { Organization } from "./Organization";

export class Role implements GeneralModel<string>, ListableItem {
    index = 0;
    id: string;
    name:string;
    tenantId: string | null;
    organization: Organization | null;

    constructor(id: string, name: string, tenantId: string | null, organization: Organization | null) {
        this.id = id;
        this.name = name;
        this.tenantId = tenantId;
        this.organization = organization;
    }
}