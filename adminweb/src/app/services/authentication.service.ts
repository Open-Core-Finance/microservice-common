import { Injectable, OnDestroy } from '@angular/core';
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

@Injectable({ providedIn: 'root' })
export class AuthenticationService implements OnDestroy {

    private currentSessionSubject: BehaviorSubject<LoginSession | null>;
    selectedRoleSubject: BehaviorSubject<Role | null> = new BehaviorSubject<Role | null>(null);
    private loginErrorSubject: BehaviorSubject<string[]> = new BehaviorSubject<string[]>([]);
    private roleListSubject: BehaviorSubject<Role[]> = new BehaviorSubject<Role[]>([]);

    public currentSession: Observable<LoginSession | null>;
    refreshInterval: Observable<number>;
    selectedRoleObservable = this.selectedRoleSubject.asObservable();
    loginErrorObservable = this.loginErrorSubject.asObservable();
    roleListObservable = this.roleListSubject.asObservable();

    refreshIntervalSubscription: Subscription | null = null;
    loginSubscription!: Subscription;
    roleList: Role[] = [];

    constructor(private restService: RestService, private http: HttpClient, private commonService: CommonService) {
        this.refreshInterval = interval(environment.loginRefreshInterval);
        const savedCredential = sessionStorage.getItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL);
        if (savedCredential != null) {
          this.currentSessionSubject = new BehaviorSubject<LoginSession | null>(JSON.parse(savedCredential));
          this.refreshIntervalSubscription = this.refreshInterval.subscribe(_ => this.refeshToken());
        } else {
          this.currentSessionSubject = new BehaviorSubject<LoginSession | null>(null);
        }
        this.currentSession = this.currentSessionSubject.asObservable();
        this.loginSubscription = this.currentSession.subscribe(x => restService.loginSession = x);
    }

    ngOnDestroy(): void {
        this.loginSubscription.unsubscribe();
        this.refreshIntervalSubscription?.unsubscribe();
    }

    public get currentSessionValue(): LoginSession | null {
        return this.currentSessionSubject.value;
    }

    public login(formData: Record<string, any>, callback: (data: GeneralApiResponse) => void) : void {
        let headers = this.restService.initRequestHeaders();
        const serviceUrl = environment.apiUrl.authentication + environment.apiPrefix.userLogin;
        const requestBody = this.commonService.buildPostStringBody(formData);
        this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
            next: (data: any) => {
                if (data.status === 0) {
                    this.saveSession(data.result as LoginSession);
                    if (!this.refreshIntervalSubscription) {
                        this.refreshIntervalSubscription = this.refreshInterval.subscribe(
                            _ => this.refeshToken()
                        );
                    }
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
                console.error(data);
                this.logout();
            }
        });
    }

    public saveSession(session: LoginSession) {
        this.currentSessionSubject.next(session);
        sessionStorage.setItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL, JSON.stringify(session));
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

    private permissionDeniedError() {
        this.loginErrorSubject.next(["permission_denied"]);
        this.roleListSubject.next([]);
    }

    logout() {
        // remove user from storage to log user out
        sessionStorage.removeItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL);
        this.selectedRoleSubject.next(null);
        this.roleListSubject.next([]);
        this.currentSessionSubject.next(null);
        this.refreshIntervalSubscription?.unsubscribe();
    }
}
