import { UserRole } from "./LoginSession";
import { Organization } from "./Organization";

export class Role {
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