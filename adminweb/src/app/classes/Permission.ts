import { GeneralModel, ListableItem } from "./CommonClasses";

export class Permission implements GeneralModel<string>, ListableItem {
    public static ANY_ROLE_APPLIED_VALUE = "ANY";

    index: number = 0;
	id = "";

    roleId = "";
    resourceType = "";
    action = "";
    url: string | null = "";

    control = AccessControl.ALLOWED;

    requestMethod: RequestMethod | null = RequestMethod.GET;

    serviceUrl = "";
}

export enum AccessControl {
    ALLOWED = "ALLOWED",
    DENIED = "DENIED",
    ALLOWED_SPECIFIC_RESOURCES = "ALLOWED_SPECIFIC_RESOURCES",
    DENIED_SPECIFIC_RESOURCES = "DENIED_SPECIFIC_RESOURCES",
    MANUAL_CHECK = "MANUAL_CHECK"
}

export enum RequestMethod {
    GET = "GET", HEAD = "HEAD", POST = "POST",
    PUT = "PUT", PATCH = "PATCH", DELETE = "DELETE",
    OPTIONS = "OPTIONS", TRACE = "TRACE"
}

export class ResourceAction {
    public static COMMON_ACTION_ADD = "add";
    public static COMMON_ACTION_UPDATE = "update";
    public static COMMON_ACTION_DELETE = "delete";
    public static COMMON_ACTION_LIST = "list";
    public static COMMON_ACTION_VIEW = "view";
    public static COMMON_ACTION_INITIAL = "initial";

    id = "";
    resourceType = "";
    action = "add";
    url = "";
    requestMethod: RequestMethod | null = RequestMethod.GET;
}

export class ResourceActionConfig extends ResourceAction {
    selected: boolean = false;

    public static fromAction(action: ResourceAction): ResourceActionConfig {
        var config = new ResourceActionConfig();
        Object.assign(config, action);
        return config;
    }
}

export class PermissionConfig {
    serviceUrl = "";
    resourceName = "";
    actions: ResourceActionConfig[] = [];
    selected = false;
    configGroup: string;
    permissions: Permission[] = [];

    constructor(serviceUrl: string, resourceName: string, configGroup: string) {
        this.serviceUrl = serviceUrl;
        this.resourceName = resourceName;
        this.configGroup = configGroup;
    }

    clone(): PermissionConfig {        
        var actions = this.actions.flatMap(a => Object.assign(new ResourceActionConfig(), a));
        var permissions = this.permissions.flatMap(p => Object.assign(new Permission(), p));
        return Object.assign(new PermissionConfig(this.serviceUrl, this.resourceName, this.configGroup), {
            actions: actions, permissions: permissions
        });
    }
}