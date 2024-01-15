import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {environment} from "../../../environments/environment";
import { ProductAvailability, ProductFee, ProductNewAccountSetting} from "../../classes/products/Product";
import {ProductCategoryType} from "../../classes/products/ProductCategory";
import { GlProduct } from 'src/app/classes/products/GlProduct';
import { GeneralAddProductComponent } from '../GeneralAddProductComponent';

@Component({
  selector: 'app-add-gl-product',
  templateUrl: './add-gl-product.component.html',
  styleUrl: './add-gl-product.component.sass'
})
export class AddGlProductComponent extends GeneralAddProductComponent<GlProduct> implements AfterViewInit, OnInit {

  addProductForm = this.formBuilder.group(
    Object.assign(Object.assign({}, new GlProduct()), {
      productAvailabilityModeInfo: new FormControl<string[]>([]),
      productAvailabilities: new FormControl<ProductAvailability[]>([]),
      newAccountSetting: this.formBuilder.group(new ProductNewAccountSetting()),
      productFees: new FormControl<ProductFee[]>([]),
      currencies: new FormControl<string[]>([])
    })
  );

  protected override getProductCategoryType(): ProductCategoryType {
    return ProductCategoryType.GL;
  }
  protected override getServiceUrl(): string {
    return environment.apiUrl.glProduct;
  }
  protected override getAddForm(): FormGroup<any> {
    return this.addProductForm;
  }
  protected override newEmptyEntity(): GlProduct {
    return new GlProduct();
  }

  ngAfterViewInit(): void {
  }
}
