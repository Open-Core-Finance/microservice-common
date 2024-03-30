import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AbstractEntityListService } from './abstract.entity.list.service';
import { Region } from '../classes/geocode/Region';
import { SubRegion } from '../classes/geocode/SubRegion';
import { Country } from '../classes/geocode/Country';
import { State } from '../classes/geocode/State';
import { City } from '../classes/geocode/City';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonService } from './common.service';
import { RestService } from './rest.service';
import { GeneralApiResponse } from '../classes/GeneralApiResponse';

@Injectable({
  providedIn: 'root'
})
export class RegionService extends AbstractEntityListService<Region> {

    override get entityServiceUrl(): string {
        return environment.apiUrl.region + "/";
    }

}

@Injectable({
    providedIn: 'root'
})
export class SubRegionService extends AbstractEntityListService<SubRegion> {

    override get entityServiceUrl(): string {
        return environment.apiUrl.subRegion + "/";
    }

}

@Injectable({
    providedIn: 'root'
})
export class CountryService extends AbstractEntityListService<Country> {

    override get entityServiceUrl(): string {
        return environment.apiUrl.country + "/";
    }

}

@Injectable({
    providedIn: 'root'
})
export class StateService extends AbstractEntityListService<State> {

    override get entityServiceUrl(): string {
        return environment.apiUrl.state + "/";
    }

}

@Injectable({
    providedIn: 'root'
})
export class CityService {

    constructor(private http: HttpClient, private commonService: CommonService, private restService: RestService) {

    }

    filterByStateId(stateId: number): Observable<GeneralApiResponse> {
        const serviceUrl = environment.apiUrl.city + "/";
        let headers = this.restService.initRequestHeaders();
        let body = {searchText: JSON.stringify({stateId:  stateId })};
        const requestBody = this.commonService.buildPostStringBody(body);
        return this.http.post<GeneralApiResponse>(serviceUrl, requestBody, {headers});
    }
}