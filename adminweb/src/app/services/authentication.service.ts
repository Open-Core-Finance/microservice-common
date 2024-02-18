import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { LoginSession } from '../classes/LoginSession';
import { RestService } from "./rest.service";
import { GeneralApiResponse } from "../classes/GeneralApiResponse";
import { environment } from "../../environments/environment";
import { HttpClient } from '@angular/common/http';
import { CommonService } from "./common.service";
import { AppSettings } from '../classes/AppSetting';

import { interval } from 'rxjs';
import { Role } from '../classes/Role';
import { Organization } from '../classes/Organization';
import { Router } from '@angular/router';
import { UserMessage } from '../classes/UserMessage';

@Injectable({ providedIn: 'root' })
export class AuthenticationService implements OnDestroy, OnInit {

    public currentSessionSubject!: BehaviorSubject<LoginSession | null>;
    public selectedRoleSubject!: BehaviorSubject<Role | null>;
    public roleListSubject: BehaviorSubject<Role[]> = new BehaviorSubject<Role[]>([]);

    refreshInterval: Observable<number>;

    refreshIntervalSubscription: Subscription | null = null;
    loginSubscription!: Subscription;
    roleList: Role[] = [];

    constructor(private restService: RestService, private http: HttpClient, private commonService: CommonService,
        private router: Router) {
        // Refresh token
        this.refreshInterval = interval(environment.loginRefreshInterval);
        // Login session
        const savedCredential = this.currentSessionValue;
        if (savedCredential != null) {
          this.currentSessionSubject = new BehaviorSubject<LoginSession | null>(savedCredential);
          this.refreshIntervalSubscription = this.refreshInterval.subscribe(_ => this.refeshToken());
        } else {
          this.currentSessionSubject = new BehaviorSubject<LoginSession | null>(null);
        }
        // Selected role
        this.selectedRoleSubject = new BehaviorSubject<Role | null>(this.selectedRoleValue);
    }

    ngOnInit(): void {
    }

    ngOnDestroy(): void {
        this.loginSubscription.unsubscribe();
        this.refreshIntervalSubscription?.unsubscribe();
    }

    public get currentSessionValue(): LoginSession | null {
        const savedCredential = sessionStorage.getItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL);
        if (savedCredential != null) {
          return JSON.parse(savedCredential) as LoginSession;
        }
        return null;
    }

    public login(formData: Record<string, any>, userMessage: UserMessage, callback: (data: GeneralApiResponse) => void,
            errorCallback: (data: GeneralApiResponse) => void) : void {
        let headers = this.restService.initRequestHeaders();
        const serviceUrl = environment.apiUrl.authentication + environment.apiPrefix.userLogin;
        const requestBody = this.commonService.buildPostStringBody(formData);
        this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
            next: (data: any) => {
                if (data.status === 0) {
                    this.saveSession(data.result as LoginSession, userMessage);
                    this.refreshIntervalSubscription?.unsubscribe();
                    this.refreshIntervalSubscription = this.refreshInterval.subscribe(
                        _ => this.refeshToken()
                    );
                } else {
                    if (errorCallback) {
                        errorCallback(data);
                    }
                }
                if (callback) {
                    callback(data);
                }
            }, error: (data: any) => {
                if (errorCallback) {
                    errorCallback(data);
                }
            }
        });
    }

    refeshToken(): void {
        console.log("=======================", "Refreshing token...");
        const savedCredential = sessionStorage.getItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL);
        if (savedCredential == null) {
           this.logout();
           return;
        }
        var credential = this.currentSessionValue;
        let headers = this.restService.initRequestHeaders();
        const serviceUrl = environment.apiUrl.authentication + environment.apiPrefix.refreshToken;
        const requestBody = this.commonService.buildPostStringBody({
            loginId: credential?.loginId,
            refreshToken: credential?.refreshToken
        });
        this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.saveSession(data.result as LoginSession, new UserMessage([], []));
                } else {
                    this.logout();
                }
            }, error: (data: any) => {
                this.logout();
            }
        });
    }

    public saveSession(session: LoginSession, userMessage: UserMessage) {
        // Save session
        sessionStorage.setItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL, JSON.stringify(session));
        this.currentSessionSubject.next(session);
        // Load selected role
        var roleCount = session.userRoles.length;
        if (roleCount <= 0) {
            this.permissionDeniedError(userMessage);
        }
        this.roleList = [];
        var errCount = 0;
        var successCount = 0;
        session.userRoles.forEach(role => {
            if (role.resourceId != null && role.resourceId.trim().length > 0) {
                let headers = this.restService.initRequestHeaders();
                const serviceUrl = environment.apiUrl.organization + "/" + role.resourceId;
                this.http.get<GeneralApiResponse>(serviceUrl, { headers }).subscribe({
                    next: (data: GeneralApiResponse) => {
                        if (data.status === 0) {
                            var org = data.result as Organization;
                            var r = new Role(role.roleId, role.roleName, role.resourceId, org);
                            this.roleList.push(r);
                            if (++successCount + errCount >= roleCount) {
                                this.roleListSubject.next(this.roleList);
                            }
                        } else if (++errCount + successCount >= roleCount) {
                            this.permissionDeniedError(userMessage);
                        }
                    }, error: (data: any) => {
                        if (++errCount >= roleCount) {
                            this.permissionDeniedError(userMessage);
                        }
                    }
                });
            } else {
                var r = new Role(role.roleId, role.roleName, role.resourceId, null);
                this.roleList.push(r);
                if (++successCount + errCount >= roleCount) {
                    this.roleListSubject.next(this.roleList);
                }
            }
        });
    }

    public saveSelectedRole(role: Role) {
        sessionStorage.setItem(AppSettings.LOCAL_KEY_SAVED_SELECTED_ROLE, JSON.stringify(role));
        this.selectedRoleSubject.next(role);
    }

    public get selectedRoleValue(): Role | null {
        const savedSelectedRole = sessionStorage.getItem(AppSettings.LOCAL_KEY_SAVED_SELECTED_ROLE);
        if (savedSelectedRole != null) {
            return JSON.parse(savedSelectedRole) as Role;
        }
        return null;
    }

    private permissionDeniedError(userMessage: UserMessage) {
        userMessage.error = ["permission_denied"];
        this.roleListSubject.next([]);
    }

    logout() {
        // remove user from storage to log user out
        sessionStorage.removeItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL);
        sessionStorage.removeItem(AppSettings.LOCAL_KEY_SAVED_SELECTED_ROLE);
        sessionStorage.removeItem(AppSettings.LOCAL_KEY_SAVED_ORGANIZATION);
        this.selectedRoleSubject.next(null);
        this.roleListSubject.next([]);
        this.currentSessionSubject.next(null);
        this.refreshIntervalSubscription?.unsubscribe();
        this.router.navigate([environment.frontEndUrl.login]);
    }
    
}
