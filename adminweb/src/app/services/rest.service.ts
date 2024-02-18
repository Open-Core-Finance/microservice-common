import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AppPlatform, LoginSession} from "../classes/LoginSession";
import {UserMessage, MessageItem} from "../classes/UserMessage";
import {BehaviorSubject} from "rxjs";
import {GeneralApiResponse} from "../classes/GeneralApiResponse";
import {LanguageService} from "./language.service";
import { AppSettings } from '../classes/AppSetting';
import { environment } from 'src/environments/environment';
import { v4 as uuidv4 } from 'uuid';
import { OrganizationService } from './organization.service';

@Injectable({
    providedIn: 'root'
})
export class RestService {

    languageData: Record<string, any> = {};
    siteUrl: string;

    constructor(private http: HttpClient, private languageService: LanguageService, private organizationService: OrganizationService) {
        languageService.languageDataSubject.subscribe(languageData => {
            this.languageData = languageData;
        });
        this.siteUrl = window.location.href.replace("/index.html", "");
    }

    public addGeneralHeaders(headers: HttpHeaders): HttpHeaders {
        if (this.languageData['languageKey']) {
            headers = headers.set(AppSettings.REST_HEADER_LANGUAGE_KEY, this.languageData['languageKey']);
        }
        var loginSession = this.currentSessionValue;
        if (loginSession && loginSession.token) {
            headers = headers.set('Authorization', 'Bearer ' + loginSession.token);
        }

        var org = this.organizationService.organization;
        if (org && org.id) {
            headers = headers.set('x-tenant-id', org.id);
        }
        headers = headers.set(AppSettings.REST_HEADER_APP_PLATFORM, AppPlatform.WEB);
        headers = headers.set(AppSettings.REST_HEADER_APP_VERSION, environment.appVersion);
        headers = headers.set(AppSettings.REST_HEADER_CLIENT_APP_ID, environment.appClientId);
        headers = headers.set(AppSettings.REST_HEADER_DEVICE_ID, this.getDeviceId());
        headers = headers.set(AppSettings.REST_HEADER_TRACE_ID, uuidv4());
        headers = headers.set(AppSettings.REST_HEADER_SKIP_CORS, "true");
        return headers;
    }

    initRequestHeaders(): HttpHeaders {
        let headers = new HttpHeaders();
        headers = headers.set('Content-Type', 'application/x-www-form-urlencoded');
        return this.addGeneralHeaders(headers);
    }

    initApplicationJsonRequestHeaders(): HttpHeaders {
        let headers = new HttpHeaders();
        headers = headers.set('Content-Type', 'application/json');
        return this.addGeneralHeaders(headers);
    }

    handleRestError(data: any, message: UserMessage) {
        var errArr = message['error'];
        if (data.errorKeysWithData) {
            this.mapError(message, data.errorKeysWithData);
        } else if (data.statusText) {
            errArr.push(data.statusText);
        } else if (data.error) {
            var err = data.error;
            if (data.errorKeysWithData) {
                this.mapError(message, data.errorKeysWithData);
            } else if (err.result) {
                errArr.push(err.result as string[]);
            } else if (err.statusText) {
                errArr.push(err.statusText);
            } else if (err.statusCode) {
                errArr.push(err.statusCode);
            } else {
                errArr.push("Unknown error: " + JSON.stringify(err));
            }
        } else {
            errArr.push("Unknown error: " + JSON.stringify(data));
        }
    }

    private mapError(message: UserMessage, errorKeys: any) {
        var errArr = message['error'];
        Object.keys(errorKeys).forEach( key => {
            const value = errorKeys[key] as string[];
            const errorItem = new MessageItem;
            errorItem.key = key;
            errorItem.data = value;
            errArr.push(errorItem);
        });
    }

    extractSingleErrorMsg(err: any) {
        if (err.error) {
            err = err.error as GeneralApiResponse;
        }
        if (err.statusText) {
            return err.statusText;
        } else if (err.statusCode) {
            return err.statusCode;
        } else {
            return "Unknown error: " + JSON.stringify(err);
        }
    }

    getSiteUrl() {
        return this.siteUrl;
    }

    getDeviceId() {
        let deviceId: string | null = localStorage.getItem(AppSettings.REST_HEADER_DEVICE_ID)
        if(!deviceId) {
            deviceId = uuidv4()
            if (deviceId) {
                localStorage.setItem(AppSettings.REST_HEADER_DEVICE_ID, deviceId)
            }
        }
        return deviceId
    }

    public get currentSessionValue(): LoginSession | null {
        const savedCredential = sessionStorage.getItem(AppSettings.LOCAL_KEY_SAVED_CREDENTIAL);
        if (savedCredential != null) {
          return JSON.parse(savedCredential) as LoginSession;
        }
        return null;
    }
}
