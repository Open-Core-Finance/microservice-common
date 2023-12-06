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

@Injectable({ providedIn: 'root' })
export class AuthenticationService implements OnDestroy {

    private currentSessionSubject: BehaviorSubject<LoginSession | null>;
    public currentSession: Observable<LoginSession | null>;
    loginSession: LoginSession | null = null;
    refreshIntervalSubscription: Subscription | null = null;
    loginSubscription: Subscription | null = null;
    refreshInterval: Observable<number>;

    constructor(private restService: RestService, private http: HttpClient, private commonService: CommonService) {
        this.refreshInterval = interval(environment.loginRefreshInterval);
        const savedCredential = sessionStorage.getItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL);
        if (savedCredential != null) {
          this.currentSessionSubject = new BehaviorSubject<LoginSession | null>(JSON.parse(savedCredential));
          this.refreshIntervalSubscription = this.refreshInterval.subscribe(
                _ => this.refeshToken()
            );
        } else {
          this.currentSessionSubject = new BehaviorSubject<LoginSession | null>(null);
        }
        this.currentSession = this.currentSessionSubject.asObservable();
        this.loginSubscription = this.currentSession.subscribe(x => {
            restService.loginSession = x;
            this.loginSession = x;
        });
    }

    ngOnDestroy(): void {
        if (this.loginSubscription) {
            this.loginSubscription.unsubscribe();
        }
        if (this.refreshIntervalSubscription) {
            this.refreshIntervalSubscription.unsubscribe();
        }
    }

    public get currentSessionValue(): LoginSession | null {
        return this.currentSessionSubject.value;
    }

    public login(formData: Record<string, any>, callback: (data: GeneralApiResponse) => void): void {
        let headers = this.restService.initRequestHeaders();
        const serviceUrl = environment.apiUrl.authentication + environment.apiPrefix.userLogin;
        const requestBody = this.commonService.buildPostStringBody(formData);
        this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.saveSession(data.result as LoginSession);
                }
                if (!this.refreshIntervalSubscription) {
                    this.refreshIntervalSubscription = this.refreshInterval.subscribe(
                        _ => this.refeshToken()
                    );
                }
                callback(data);
            }, error: (data: GeneralApiResponse) => {
                console.error(data);
                callback(data);
            }
        });
    }

    refeshToken(): void {
        const savedCredential = sessionStorage.getItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL);
        if (savedCredential == null) {
           this.logout();
           return;
        }
        var credential = JSON.parse(savedCredential);
        let headers = this.restService.initRequestHeaders();
        const serviceUrl = environment.apiUrl.authentication + environment.apiPrefix.refreshToken;
        const requestBody = this.commonService.buildPostStringBody({
            loginId: credential['loginId'],
            refreshToken: credential['refreshToken']
        });
        this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.saveSession(data.result as LoginSession);
                } else {
                    this.logout();
                }
            }, error: (data: GeneralApiResponse) => {
                console.error(data);
                this.logout();
            }
        });
    }

    public saveSession(session: LoginSession) {
        sessionStorage.setItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL, JSON.stringify(session));
        this.currentSessionSubject.next(session);
    }

    logout() {
        // remove user from storage to log user out
        sessionStorage.removeItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL);
        this.currentSessionSubject.next(null);
        if (this.refreshIntervalSubscription) {
            this.refreshIntervalSubscription.unsubscribe();
        }
    }
}
