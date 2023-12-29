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

@Injectable({ providedIn: 'root' })
export class AuthenticationService implements OnDestroy, OnInit {

    private currentSessionSubject!: BehaviorSubject<LoginSession | null>;
    private selectedRoleSubject!: BehaviorSubject<Role | null>;
    private loginErrorSubject: BehaviorSubject<string[]> = new BehaviorSubject<string[]>([]);
    private roleListSubject: BehaviorSubject<Role[]> = new BehaviorSubject<Role[]>([]);

    public currentSession!: Observable<LoginSession | null>;
    refreshInterval: Observable<number>;
    selectedRoleObservable!: Observable<Role | null>;
    loginErrorObservable = this.loginErrorSubject.asObservable();
    roleListObservable = this.roleListSubject.asObservable();

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
        this.currentSession = this.currentSessionSubject.asObservable();
        // Selected role
        this.selectedRoleSubject = new BehaviorSubject<Role | null>(this.selectedRoleValue);
        this.selectedRoleObservable = this.selectedRoleSubject.asObservable();
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

    public login(formData: Record<string, any>, callback: (data: GeneralApiResponse) => void) : void {
        let headers = this.restService.initRequestHeaders();
        const serviceUrl = environment.apiUrl.authentication + environment.apiPrefix.userLogin;
        const requestBody = this.commonService.buildPostStringBody(formData);
        this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
            next: (data: any) => {
                if (data.status === 0) {
                    this.saveSession(data.result as LoginSession);
                    this.refreshIntervalSubscription?.unsubscribe();
                    this.refreshIntervalSubscription = this.refreshInterval.subscribe(
                        _ => this.refeshToken()
                    );
                } else {
                    this.buildLoginError(data);
                }
                if (callback) {
                    callback(data);
                }
            }, error: (data: any) => {
                console.error(data);
                if (callback) {
                    callback(data);
                }
                this.buildLoginError(data);
            }
        });
    }

    private buildLoginError(data: any) {
        var err = data.error;
        if (err.statusCode) {
            this.loginErrorSubject.next([err.statusCode as string]);
        } else if (err.message != null) {
            this.loginErrorSubject.next([err.message as string]);
        } else {
            this.loginErrorSubject.next([data as string]);
        }
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
                    this.saveSession(data.result as LoginSession);
                } else {
                    this.logout();
                }
            }, error: (data: any) => {
                this.logout();
            }
        });
    }

    public saveSession(session: LoginSession) {
        // Save session
        sessionStorage.setItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL, JSON.stringify(session));
        this.currentSessionSubject.next(session);
        // Load selected role
        var roleCount = session.userRoles.length;
        if (roleCount <= 0) {
            this.permissionDeniedError();
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
                            this.permissionDeniedError();
                        }
                    }, error: (data: any) => {
                        if (++errCount >= roleCount) {
                            this.permissionDeniedError();
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

    private permissionDeniedError() {
        this.loginErrorSubject.next(["permission_denied"]);
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
