import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { GeneralApiResponse } from "../classes/GeneralApiResponse";
import { environment } from "src/environments/environment";
import { RestService } from "./rest.service";
import { CommonService } from "./common.service";
import { HttpClient } from "@angular/common/http";

@Injectable()
export abstract class AbstractCustomerService {
    constructor(private restService: RestService, private commonService: CommonService, private http: HttpClient) {
    }

    filterCustomer(filterText: string): Observable<GeneralApiResponse> {
        if (filterText.length < 1) {
            const result = new GeneralApiResponse();
            result.result = [];
            return of(result);
        }
        let headers = this.restService.initRequestHeaders();
        let searchText = '{"' + this.autocompleteAttr + '":"' +  filterText.trim() + '"}';
        let body = {searchText: searchText};
        const requestBody = this.commonService.buildPostStringBody(body);
        return this.http.post<GeneralApiResponse>(this.serviceUrl, requestBody, {headers});
    }

    abstract get serviceUrl(): string;

    abstract get autocompleteAttr(): string;
}

@Injectable({ providedIn: 'root' })
export class InvidualCustomerService extends AbstractCustomerService {

    override get autocompleteAttr(): string {
        return "firstName";
    }

    override get serviceUrl(): string {
        return environment.apiUrl.individualCustomer + "/";
    }

}

@Injectable({ providedIn: 'root' })
export class CorporateCustomerService extends AbstractCustomerService {

    override get serviceUrl(): string {
        return environment.apiUrl.corporateCustomer + "/";
    }

    override get autocompleteAttr(): string {
        return "name";
    }
}