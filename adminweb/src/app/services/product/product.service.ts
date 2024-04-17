import { Injectable } from '@angular/core';
import { AbstractEntityListService } from '../abstract.entity.list.service';
import { GlProduct } from 'src/app/classes/products/GlProduct';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { CommonService } from '../common.service';
import { RestService } from '../rest.service';
import { Observable } from 'rxjs';
import { GeneralApiResponse } from 'src/app/classes/GeneralApiResponse';
import { ProductCategoryType } from 'src/app/classes/products/ProductCategory';
import { DepositProduct } from 'src/app/classes/products/DepositProduct';
import { CryptoProduct } from 'src/app/classes/products/CryptoProduct';
import { LoanProduct } from 'src/app/classes/products/LoanProduct';

@Injectable({
  providedIn: 'root'
})
export class GlProductService extends AbstractEntityListService<GlProduct> {

  override get entityServiceUrl(): string {
    return environment.apiUrl.glProduct + "/";
  }

}

@Injectable({
  providedIn: 'root'
})
export class ProductCategoryService {

  constructor(private http: HttpClient, private commonService: CommonService, private restService: RestService) {

  }

  filterByType(type: ProductCategoryType): Observable<GeneralApiResponse> {
      const serviceUrl = environment.apiUrl.productCategory + "/";
      let headers = this.restService.initRequestHeaders();
      let body = {searchText: JSON.stringify({type:  type })};
      const requestBody = this.commonService.buildPostStringBody(body);
      return this.http.post<GeneralApiResponse>(serviceUrl, requestBody, {headers});
  }

}

@Injectable({
  providedIn: 'root'
})
export class ProductTypeService {

  constructor(private http: HttpClient, private commonService: CommonService, private restService: RestService) {

  }

  filterByType(type: ProductCategoryType): Observable<GeneralApiResponse> {
      const serviceUrl = environment.apiUrl.productType + "/";
      let headers = this.restService.initRequestHeaders();
      let body = {searchText: JSON.stringify({type:  type })};
      const requestBody = this.commonService.buildPostStringBody(body);
      return this.http.post<GeneralApiResponse>(serviceUrl, requestBody, {headers});
  }

}

@Injectable({
  providedIn: 'root'
})
export class DepositProductService extends AbstractEntityListService<DepositProduct> {

  override get entityServiceUrl(): string {
    return environment.apiUrl.depositProduct + "/";
  }

}

@Injectable({
  providedIn: 'root'
})
export class CryptoProductService extends AbstractEntityListService<CryptoProduct> {

  override get entityServiceUrl(): string {
    return environment.apiUrl.cryptoProduct + "/";
  }

}

@Injectable({
  providedIn: 'root'
})
export class LoanProductService extends AbstractEntityListService<LoanProduct> {

  override get entityServiceUrl(): string {
    return environment.apiUrl.loanProduct + "/";
  }

}
