import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { GeneralApiResponse } from "../classes/GeneralApiResponse";
import { environment } from "src/environments/environment";
import { RestService } from "./rest.service";
import { CommonService } from "./common.service";
import { HttpClient } from "@angular/common/http";
import { CustomerType } from "../classes/customers/CustomerType";

@Injectable()
export abstract class AbstractDepositAccountService {
    constructor(private restService: RestService, private commonService: CommonService, private http: HttpClient) {
    }

    filterAccount(filterText: string, customerId: number, customerType: CustomerType): Observable<GeneralApiResponse> {
        let headers = this.restService.initRequestHeaders();
        let searchText = '{"' + this.autocompleteAttr + '":"' +  filterText.trim() + '","customerId":' + customerId +
            ",\"customerType\":\"" + customerType + '"}';
        let body = {searchText: searchText};
        const requestBody = this.commonService.buildPostStringBody(body);
        return this.http.post<GeneralApiResponse>(this.serviceUrl, requestBody, {headers});
    }

    abstract get serviceUrl(): string;

    get autocompleteAttr(): string {
        return "id";
    }
}

@Injectable({ providedIn: 'root' })
export class DepositAccountService extends AbstractDepositAccountService {

    override get serviceUrl(): string {
        return environment.apiUrl.depositAccount + "/";
    }

}

@Injectable({ providedIn: 'root' })
export class CryptoAccountService extends AbstractDepositAccountService {

    override get serviceUrl(): string {
        return environment.apiUrl.cryptoAccount + "/";
    }
}