import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AppPlatform, LoginSession} from "../classes/LoginSession";
import {UserMessage} from "../classes/UserMessage";
import {BehaviorSubject} from "rxjs";
import {GeneralApiResponse} from "../classes/GeneralApiResponse";
import {LanguageService} from "./language.service";
import { AppSettings } from '../classes/AppSetting';
import { environment } from 'src/environments/environment';
import { v4 as uuidv4 } from 'uuid';

@Injectable({
    providedIn: 'root'
})
export class RestService {

    loginSession: LoginSession | null = null;
    languageData: Record<string, any> = {};
    siteUrl: string;

    constructor(private http: HttpClient, private languageService: LanguageService) {
        languageService.languageDataObservable.subscribe(languageData => {
            this.languageData = languageData;
        });
        this.siteUrl = window.location.href.replace("/index.html", "");
    }

    public addGeneralHeaders(headers: HttpHeaders): HttpHeaders {
        if (this.languageData['languageKey']) {
            headers = headers.set(AppSettings.REST_HEADER_LANGUAGE_KEY, this.languageData['languageKey']);
        }
        if (this.loginSession && this.loginSession.token) {
            headers = headers.set('Authorization', 'Bearer ' + this.loginSession.token);
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

    handleRestError(err: any, messageSubject: BehaviorSubject<UserMessage>) {
        if (err.error) {
            err = err.error as GeneralApiResponse;
        }
        const message = new UserMessage([], []);
        if (err.statusText) {
            message.error.push(err.statusText);
        } else if (err.statusCode) {
            const codes = err.statusCode.split('\n');
            for (const code of codes) {
                message.error.push(code);
            }
        } else {
            message.error.push("Unknown error: " + JSON.stringify(err));
        }
        messageSubject.next(message);
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
}
