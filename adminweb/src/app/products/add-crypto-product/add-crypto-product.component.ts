import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {environment} from "../../../environments/environment";
import { ProductAvailability, ProductFee, ProductNewAccountSetting} from "../../classes/products/Product";
import { CryptoProduct } from 'src/app/classes/products/CryptoProduct';
import { GeneralAddProductComponent } from '../GeneralAddProductComponent';
import { ProductCategoryType } from 'src/app/classes/products/ProductCategory';

@Component({
  selector: 'app-add-crypto-product',
  templateUrl: './add-crypto-product.component.html',
  styleUrl: './add-crypto-product.component.sass'
})
export class AddCryptoProductComponent extends GeneralAddProductComponent<CryptoProduct> implements AfterViewInit, OnInit {

  addProductForm = this.formBuilder.group(
    Object.assign(Object.assign({}, new CryptoProduct()), {
      productAvailabilityModeInfo: new FormControl<string[]>([]),
      productAvailabilities: new FormControl<ProductAvailability[]>([]),
      newAccountSetting: this.formBuilder.group(new ProductNewAccountSetting()),
      productFees: new FormControl<ProductFee[]>([]),
      currencies: new FormControl<string[]>([])
    })
  );

  ngAfterViewInit(): void {
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.cryptoProduct;
  }

  protected override getAddForm(): FormGroup<any> {
    return this.addProductForm;
  }

  protected override validateFormData(formData: any): void {
    super.validateFormData(formData);
  }

  protected override newEmptyEntity(): CryptoProduct {
    return new CryptoProduct();
  }

  protected override getProductCategoryType(): ProductCategoryType {
    return ProductCategoryType.CRYPTO;
  }
}
