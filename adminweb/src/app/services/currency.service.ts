import { Injectable, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { RestService } from './rest.service';
import { environment } from 'src/environments/environment';
import { GeneralApiResponse } from '../classes/GeneralApiResponse';
import { HttpClient } from '@angular/common/http';
import { Currency } from '../classes/Currency';
import { CommonService } from './common.service';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService implements OnInit {

    public currenciesSubject: BehaviorSubject<Currency[]> = new BehaviorSubject<Currency[]>([]);

    constructor(private restService: RestService, private http: HttpClient, private commonService: CommonService) {
        // Init
        this.ngOnInit();
    }

    ngOnInit(): void {
        let headers = this.restService.initRequestHeaders();
        const serviceUrl = environment.apiUrl.currency + "/";
        const requestBody = this.commonService.buildPostStringBody({pageSize: -1, pageIndex: -1});
        this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.currenciesSubject.next(data.result as Currency[]);
                }
            }, error: (data: GeneralApiResponse) => {
                console.error(data);
            }
        });
    }
}