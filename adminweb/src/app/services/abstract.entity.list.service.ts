import { Injectable, OnDestroy, OnInit } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { CommonService } from "./common.service";
import { HttpClient } from "@angular/common/http";
import { RestService } from "./rest.service";
import { GeneralApiResponse } from "../classes/GeneralApiResponse";

@Injectable()
export abstract class AbstractEntityListService<T> {

    public entityListSubject: BehaviorSubject<T[]>;

    constructor(protected restService: RestService, protected http: HttpClient, protected commonService: CommonService) {
        this.entityListSubject = new BehaviorSubject<T[]>([]);
        this.reloadEntities();
    }

    protected reloadEntities() {
        let headers = this.restService.initRequestHeaders();
        const serviceUrl = this.entityServiceUrl;
        const requestBody = this.commonService.buildPostStringBody({pageSize: -1, pageIndex: -1});
        this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.entityListSubject.next(data.result as T[]);
                }
            }, error: (data: GeneralApiResponse) => {
                console.error(data);
            }
        });
    }

    abstract get entityServiceUrl(): string;
}