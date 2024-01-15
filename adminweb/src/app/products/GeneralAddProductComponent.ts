import { Component, OnInit } from "@angular/core";
import { Product, ProductNewAccountSettingType } from "../classes/products/Product";
import { ProductCategory, ProductCategoryType } from "../classes/products/ProductCategory";
import { ProductType } from "../classes/products/ProductType";
import { GeneralEntityAddComponent } from "../generic-component/GeneralEntityAddComponent";
import { GeneralApiResponse } from "../classes/GeneralApiResponse";
import { environment } from "src/environments/environment";

@Component({
    template: ''
})
export abstract class GeneralAddProductComponent<T extends Product> extends GeneralEntityAddComponent<T> implements OnInit {

    protected productCategories: ProductCategory[] = [];
    protected productTypes: ProductType[] = [];
    protected newAccountIdTypeEnum = ProductNewAccountSettingType;

    protected override validateFormData(formData: any): void {
        if (this.commonService.isNullOrEmpty(formData.name)) {
            this.message['error'].push("name_empty")
        }
    }

    protected abstract getProductCategoryType(): ProductCategoryType;

    ngOnInit(): void {
        let headers = this.restService.initRequestHeaders();
        this.http.get<GeneralApiResponse>(environment.apiUrl.productCategory + "/", { headers, params: {
            type: this.getProductCategoryType()
        }}).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.productCategories = (data.result as ProductCategory[]);
                }
            }, error: (data: any) => this.restService.handleRestError(data, this.message)
        });
        this.http.get<GeneralApiResponse>(environment.apiUrl.productType + "/", { headers, params: {
            type: this.getProductCategoryType()
        } }).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.productTypes = (data.result as ProductType[]);
                }
            }, error: (data: any) => this.restService.handleRestError(data, this.message)
        });
    }
}