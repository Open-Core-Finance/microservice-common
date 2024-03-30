import { Component } from '@angular/core';
import { ProductCategory, ProductCategoryType } from 'src/app/classes/products/ProductCategory';
import { environment } from 'src/environments/environment';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';

@Component({
  selector: 'app-add-product-category',
  templateUrl: './add-product-category.component.html',
  styleUrl: './add-product-category.component.sass'
})
export class AddProductCategoryComponent extends GeneralEntityAddComponent<ProductCategory>{

  productCategoryTypes = Object.keys(ProductCategoryType);

  protected override getServiceUrl(): string {
    return environment.apiUrl.productCategory;
  }

  protected override validateFormData(formData: any): void {
    if (this.commonService.isNullOrEmpty(formData.name)) {
      this.message['error'].push("name_empty");
    }
    if (formData.type == null) {
      this.message['error'].push("type_empty");
    }
  }
  protected override newEmptyEntity(): ProductCategory {
    return new ProductCategory();
  }
}
