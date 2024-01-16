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
export abstract class GeneralProductAddComponent<T extends Product> extends GeneralEntityAddComponent<T> implements OnInit {

    protected productCategories: ProductCategory[] = [];
    protected productTypes: ProductType[] = [];
    protected newAccountIdTypeEnum = ProductNewAccountSettingType;

    protected override validateFormData(formData: any): void {
        var errs = this.message['error'] as string[];
        if (this.commonService.isNullOrEmpty(formData.name)) {
            errs.push("product_name_empty");
        }
        if (this.commonService.isNullOrEmpty(formData.category)) {
            errs.push("product_category_empty");
        }
        if (this.commonService.isNullOrEmpty(formData.type)) {
            errs.push("type_empty");
        }
        if (this.commonService.isNullOrEmpty(formData.newAccountSetting.type)) {
            errs.push("new_account_type_empty");
        }
        if (formData.currencies.length < 1) {
            errs.push("currencies_empty");
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
                } else this.restService.handleRestError(data, this.message)
            }, error: (data: any) => this.restService.handleRestError(data, this.message)
        });
        this.http.get<GeneralApiResponse>(environment.apiUrl.productType + "/", { headers, params: {
            type: this.getProductCategoryType()
        } }).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.productTypes = (data.result as ProductType[]);
                } else this.restService.handleRestError(data, this.message)
            }, error: (data: any) => this.restService.handleRestError(data, this.message)
        });
    }
}