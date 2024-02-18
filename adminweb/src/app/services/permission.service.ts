import { Injectable, OnDestroy } from '@angular/core';
import { AccessControl, Permission, PermissionConfig, ResourceAction, ResourceActionConfig } from '../classes/Permission';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { RestService } from './rest.service';
import { CommonService } from './common.service';
import { GeneralApiResponse } from '../classes/GeneralApiResponse';
import { BehaviorSubject, Subscription } from 'rxjs';
import { AuthenticationService } from './authentication.service';
import { LoginSession } from '../classes/LoginSession';

@Injectable({
  providedIn: 'root'
})
export class PermissionService implements OnDestroy {

  // Permission config cached
  public currentPermissionConfigs: Map<string,PermissionConfig[]> = new Map();
  // Permission config subscription
  public permissionConfigSubject: BehaviorSubject<Map<string,PermissionConfig[]>>;
  private permissionConfigSubscription: Subscription | undefined;
  // Errors
  public permissionErrorSubject: BehaviorSubject<any>;

  public currentUserPermissions: Permission[] = [];
  public currentUserPermissionSubject: BehaviorSubject<Permission[]>;
  private currentUserPermissionSubscription: Subscription | undefined;

  constructor(private restService: RestService, private commonService: CommonService, private http: HttpClient,
    private authenticationService: AuthenticationService) {
    // Permission config
    this.permissionConfigSubject = new BehaviorSubject<Map<string,PermissionConfig[]>>(new Map());
    this.permissionConfigSubscription?.unsubscribe();
    // Permission config cache
    this.permissionConfigSubscription = this.permissionConfigSubject.subscribe(p => {
      this.currentPermissionConfigs = p;
    });
    // Permission error
    this.permissionErrorSubject = new BehaviorSubject(null);
    // Load permissions configs
    this.reloadPermissionConfig();
    // Current user permissions
    this.currentUserPermissionSubject = new BehaviorSubject<Permission[]>([]);
    this.currentUserPermissionSubscription?.unsubscribe();
    this.currentUserPermissionSubscription = this.currentUserPermissionSubject.subscribe(p => this.currentUserPermissions = p);
    this.authenticationService.currentSessionSubject.subscribe(s => this.reloadCurrentUserPermissions(s));
  }

  ngOnDestroy(): void {
    this.permissionConfigSubscription?.unsubscribe();
    this.currentUserPermissionSubscription?.unsubscribe();
  }

  reloadPermissionConfig(): void {
    const permissionConfigs: PermissionConfig[] = [];
    var services = this.getServiceUrls();
    let completedCount = 0;
    let totalCount = services.length;
    services.forEach( s => {
      const serviceUrl = s + environment.apiPrefix.resourceActions + "/";
      let headers = this.restService.initRequestHeaders();
      const requestBody = this.commonService.buildPostStringBody({pageSize: -1, pageIndex: -1});
      this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
          next: (data: GeneralApiResponse) => {
            if (data.status === 0) {
              this.mapResourceActions(data.result as ResourceAction[], s, permissionConfigs);
            }
            this.checkCompleted(++completedCount, totalCount, permissionConfigs);
          }, error: (data: any) => {
            ++completedCount;
            this.permissionErrorSubject.next(data);
          }
      });
    });
  }

  private getConfigGroupForUrl(serviceUrl: string): string {
    switch(serviceUrl) {
      case environment.apiUrl.userprofile:
        return "userprofile";
      case environment.apiUrl.product:
        return "product";
      default:
        return "others";
    }
  }

  private mapResourceActions(resourceActions: ResourceAction[], serviceUrl: string, configs: PermissionConfig[]) {
    for (let a of resourceActions) {
      let found = false;
      for (let p of configs) {
        if (p.serviceUrl == serviceUrl && p.resourceName == a.resourceType) {
          found = true;
          p.actions.push(ResourceActionConfig.fromAction(a));
          break;
        }
      }
      if (!found) {
        const configGroup = this.getConfigGroupForUrl(serviceUrl);
        let permissionConfig = new PermissionConfig(serviceUrl, a.resourceType, configGroup);
        permissionConfig.actions.push(ResourceActionConfig.fromAction(a));
        configs.push(permissionConfig);
      }
    }
  }

  private checkCompleted(completedCount: number, totalCount: number, configs: PermissionConfig[]) {
    if (completedCount >= totalCount) {
      let map: Map<string, PermissionConfig[]> = new Map();
      for (let config of configs) {
        let valueArr = map.get(config.configGroup);
        if (!valueArr) {
          valueArr = [];
          map.set(config.configGroup, valueArr);
        }
        valueArr.push(config);
      }
      this.permissionConfigSubject.next(map);
    }
  }

  public loadPermissionByRoles(roleIds: string[], callback: ((value: Permission[]) => void)): void {
    var permissions: Permission[] = [];
    var services = this.getServiceUrls();
    let completedCount = 0;
    let totalCount = services.length;
    services.forEach( s => {
      const serviceUrl = s + environment.apiPrefix.permissionByRoles;
      let headers = this.restService.initApplicationJsonRequestHeaders();
      this.http.post<GeneralApiResponse>(serviceUrl, roleIds, { headers }).subscribe({
          next: (data: GeneralApiResponse) => {
            if (data.status === 0) {
              for (let p of (data.result as Permission[])) {
                p.serviceUrl = s;
                permissions.push(p);
              }
            }
            this.checkLoadPermissionsCompleted(++completedCount, totalCount, permissions, callback);
          }, error: (data: any) => {
            ++completedCount;
            this.permissionErrorSubject.next(data);
          }
      });
    });
  }

  private checkLoadPermissionsCompleted(completedCount: number, totalCount: number, permissions: Permission[],
      callback: ((value: Permission[]) => void)) {
    if (completedCount >= totalCount) {
      callback(permissions);
    }
  }

  public mapPermissions(permissions: Permission[], configMap: Map<string, PermissionConfig[]>) {
    for(let p of permissions) {
      this.mapPermission(p, configMap);
    }
  }

  public mapPermission(p: Permission, configMap: Map<string, PermissionConfig[]>) {
    for(let permissionTab of configMap) {
      var values: PermissionConfig[] = permissionTab[1];
      for (let value of values) {
        if (value.serviceUrl == p.serviceUrl && value.resourceName == p.resourceType) {
          value.permissions.push(p);
          let checkAll = true;
          for (let action of value.actions) {
            if (this.checkPatternMatched(action, p)){
              action.selected = true;
            }
            if (checkAll && !action.selected) {
              checkAll = false;
            }
          }
          value.selected = checkAll;
          break;
        }
      }
    }
  }

  public checkPatternMatched(a: ResourceAction, p: Permission): boolean {
    var matchedAction = Permission.ANY_ROLE_APPLIED_VALUE == p.action.toUpperCase() || a.action.toLowerCase() == p.action.toLowerCase();
    var matchUrl = undefined == p.url || Permission.ANY_ROLE_APPLIED_VALUE == p.url.toUpperCase() || 
      a.url.toUpperCase() == p.url.toUpperCase();
    var matchedRequestMethod = p.requestMethod == null || p.requestMethod == a.requestMethod;
    if (matchedAction && matchUrl && matchedRequestMethod) {
        return this.checkPermissionControl(p);
    }
    return false;
  }

  private checkPermissionControl(permission: Permission): boolean {
    var control = permission.control;
    switch (control) {
        case AccessControl.DENIED:
          return false;
        case AccessControl.ALLOWED_SPECIFIC_RESOURCES:
          return true;
        case AccessControl.DENIED_SPECIFIC_RESOURCES:
          return true;
        default:
          return true;
    }
  }

  public getServiceUrls(): string[] {
    return [environment.apiUrl.userprofile, environment.apiUrl.product];
  }

  public filterConfigByServiceUrl(configMap: Map<string, PermissionConfig[]>, serviceUrl: string): PermissionConfig[] {
    let result: PermissionConfig[] = [];
    for(let permissionTab of configMap) {
      var values: PermissionConfig[] = permissionTab[1];
      for(let p of values) {
        if (p.serviceUrl == serviceUrl || p.resourceName == "permission" || p.resourceName == "resourceaction") {
          result.push(p);
        }
      }
    }
    return result;
  }

  public filterResourceActionByResourceNameAndAction(configMap: Map<string, PermissionConfig[]>, resourceName: string, action: string): ResourceAction | null {
    let result: PermissionConfig[] = [];
    for(let permissionTab of configMap) {
      var values: PermissionConfig[] = permissionTab[1];
      for(let p of values) {
        for (let a of p.actions) {
          if (a.action == action && a.resourceType == resourceName) {
            return a;
          }
        }
      }
    }
    return null;
  }

  public buildPermissionsFromConfig(configs: PermissionConfig[], roleId: string): Permission[] {
    let result: Permission[] = [];
    for (let config of configs) {
      if (config.selected) {
        let permission = new Permission();
        permission.action = Permission.ANY_ROLE_APPLIED_VALUE;
        permission.control = AccessControl.ALLOWED;
        permission.requestMethod = null;
        permission.resourceType = config.resourceName;
        permission.roleId = roleId;
        permission.url = null;
        result.push(permission);
      } else {
        for(let a of config.actions) {
          if (!a.selected) {
            continue;
          }
          let permission = new Permission();
          permission.action = a.action;
          permission.control = AccessControl.ALLOWED;
          permission.requestMethod = a.requestMethod;
          permission.resourceType = a.resourceType;
          permission.roleId = roleId;
          permission.url = a.url;
          result.push(permission);
        }
      }
    }
    return result;
  }

  reloadCurrentUserPermissions(session: LoginSession | null) {
    if (session == null) {
      this.currentUserPermissionSubject.next([]);
    } else {
      var roleIds = session.userRoles.flatMap(sr => sr.roleId);
      this.loadPermissionByRoles(roleIds, p => {
        this.currentUserPermissionSubject.next(p);
      });
    }
  }
}
