import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from "rxjs";
import { LanguageItem } from "../classes/LanguageItem";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Order, OrderDirection } from "../classes/Paging";
import { CommonService } from "./common.service";
import { AppSettings } from '../classes/AppSetting';

@Injectable({
    providedIn: 'root'
})
export class LanguageService {

    private languageSubject: BehaviorSubject<LanguageItem>;
    public languageObservable: Observable<LanguageItem>;
    private languageDataSubject: BehaviorSubject<Record<string, any>>;
    public languageDataObservable: Observable<Record<string, any>>;
    public languageData: Record<string, any>;
    private languageListSubject: BehaviorSubject<LanguageItem[]>;
    public languageListObservable: Observable<LanguageItem[]>;
    public selectedLanguage: LanguageItem = AppSettings.LANGUAGE_DEFAULT;

    constructor(private http: HttpClient, private commonService: CommonService) {
        try {
            const savedLanguage = localStorage.getItem(AppSettings.LOCAL_KEY_SAVED_LANGUAGE);
            if (savedLanguage != null) {
                const lang = JSON.parse(savedLanguage);
                if (lang != null) {
                    this.selectedLanguage = lang;
                }
            }
        } catch (e) {
            // TODO
            console.log(e);
        }
        this.languageSubject = new BehaviorSubject<LanguageItem>(this.selectedLanguage);
        this.languageObservable = this.languageSubject.asObservable();
        this.languageData = {};
        this.changeLanguage(this.selectedLanguage);
        this.languageDataSubject = new BehaviorSubject<Record<string, any>>(this.languageData);
        this.languageDataObservable = this.languageDataSubject.asObservable();
        this.languageDataObservable.subscribe(lang => {
            this.languageData = lang;
        })
        this.languageListSubject = new BehaviorSubject<LanguageItem[]>([]);
        this.languageListObservable = this.languageListSubject.asObservable();
        this.loadAllLanguages();
    }

    changeLanguage(languageItem: LanguageItem) {
        const languageKey: string = languageItem.languageKey;
        const name: string = languageItem.name;
        const lang = new LanguageItem(languageKey, name);
        localStorage.setItem(AppSettings.LOCAL_KEY_SAVED_LANGUAGE, JSON.stringify(lang));
        this.languageSubject.next(lang);
        this.http.get<Record<string, any>>("./assets/languages/language." + languageKey + ".json?" + new Date().getTime(), {
            headers: new HttpHeaders(), params: {}
        }).subscribe({
            next: (data: Record<string, any>) => {
                this.languageDataSubject.next(data);
            }, error: (err) => {
                console.error(err);
                // Whenever got error. Load default language.
                this.http.get<Record<string, any>>("./assets/languages/language." + AppSettings.LANGUAGE_DEFAULT.languageKey + ".json?"
                    + new Date().getTime(), {
                    headers: new HttpHeaders(), params: {}
                }).subscribe({
                    next: (data2: Record<string, any>) => {
                        this.languageDataSubject.next(data2);
                    },
                    error: (err2) => {
                        // TODO
                        console.error(err2);
                    }
                });
            }
        });
    }

    formatLanguage(languageKey: string, parameters: any[]) {
        let result = this.languageData;
        while (languageKey.indexOf(".") >= 0) {
            const prefix = languageKey.substring(0, languageKey.indexOf("."));
            const postfix = languageKey.substring(languageKey.indexOf(".") + 1);
            if (result == null) {
                return "";
            }
            result = result[prefix];
            languageKey = postfix;
        }
        if (languageKey.trim().length > 0) {
            if (result == null) {
                return "";
            }
            result = result[languageKey];
        }
        // @ts-ignore
        let finalResult = (result as string);
        if (parameters) {
            for (let i = 0; i < parameters.length; i++) {
                const strToReplace = "\$\{" + i + "\}";
                if (finalResult != null) {
                    finalResult = finalResult.replace(strToReplace, parameters[i]);
                }
            }
        }
        return finalResult;
    }

    private buildCommonPostBody() {
        const body = {
            pageSize: -1,
            pageIndex: -1,
            orders: JSON.stringify([new Order("id", OrderDirection.ASC)]),
            searchText: ""
        };
        return this.commonService.buildPostStringBody(body);
    }

    loadAllLanguages(): void {
        const languages = [
            new LanguageItem("us", "English"),
            AppSettings.LANGUAGE_DEFAULT
        ];
        this.languageListSubject.next(languages);
    }

}
