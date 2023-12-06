import { Injectable, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Observable } from 'rxjs/internal/Observable';
import { RestService } from './rest.service';
import { environment } from 'src/environments/environment';
import { CommonService } from './common.service';
import { GeneralApiResponse } from '../classes/GeneralApiResponse';
import { HttpClient } from '@angular/common/http';
import { Currency } from '../classes/Currency';

@Injectable({
  providedIn: 'root'
})
export class EntitiesService implements OnInit {
    public readonly ENTITY_TYPE_CURRENCY = "CURRENCY";

    public entitySubjectMap: Map<string, BehaviorSubject<any[]> | undefined> = 
        new Map<string, BehaviorSubject<any[]> | undefined>();
    public organizationObservableMap: Map<string, Observable<any[]> | undefined> =
        new Map<string, Observable<any[]> | undefined>();

    constructor(private restService: RestService, private commonService: CommonService,
        private http: HttpClient) {
        // Currency
        this.entitySubjectMap.set(this.ENTITY_TYPE_CURRENCY, new BehaviorSubject<any[]>([]));
        this.organizationObservableMap.set(this.ENTITY_TYPE_CURRENCY, 
            this.entitySubjectMap.get(this.ENTITY_TYPE_CURRENCY)?.asObservable()
        );
        // Init
        this.ngOnInit();
    }

    ngOnInit(): void {
        let headers = this.restService.initRequestHeaders();
        const serviceUrl = environment.apiUrl.currency + "/";
        const requestBody = {};
        this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.entitySubjectMap.get(this.ENTITY_TYPE_CURRENCY)?.next(data.result as Currency[]);
                }
            }, error: (data: GeneralApiResponse) => {
                console.error(data);
            }
        });
    }
}