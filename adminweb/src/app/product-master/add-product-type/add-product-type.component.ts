import { Component } from '@angular/core';
import { ProductCategoryType } from 'src/app/classes/products/ProductCategory';
import { FormControl, FormGroup } from '@angular/forms';
import { environment } from 'src/environments/environment';
import {ProductType} from "../../classes/products/ProductType";
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';

@Component({
  selector: 'app-add-product-type',
  templateUrl: './add-product-type.component.html',
  styleUrl: './add-product-type.component.sass'
})
export class AddProductTypeComponent extends GeneralEntityAddComponent<ProductType> {
  productCategoryTypes = Object.keys(ProductCategoryType);

  addProductTypeForm = new FormGroup({
    index: new FormControl(0),
    id: new FormControl(""),
    name: new FormControl('', {nonNullable: true}),
    type: new FormControl(ProductCategoryType.DEPOSIT, {nonNullable: true})
  });

  protected override getServiceUrl(): string {
    return environment.apiUrl.productType;
  }

  protected override getAddForm(): FormGroup<any> {
    return this.addProductTypeForm;
  }

  protected override validateFormData(formData: any): void {
    if (this.commonService.isNullOrEmpty(formData.name)) {
      this.message['error'].push("name_empty");
    }
    if (formData.type == null) {
      this.message['error'].push("type_empty");
    }
  }

  protected override newEmptyEntity(): ProductType {
    return new ProductType();
  }
}
