import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {environment} from "../../../environments/environment";
import {ProductCategoryType} from "../../classes/products/ProductCategory";
import { GeneralProductAddComponent } from '../GeneralProductAddComponent';
import { LoanProduct } from 'src/app/classes/products/LoanProduct';
import { ProductAvailability, ProductFee, ProductNewAccountSetting } from 'src/app/classes/products/Product';

@Component({
  selector: 'app-add-loan-product',
  templateUrl: './add-loan-product.component.html',
  styleUrl: './add-loan-product.component.sass'
})
export class AddLoanProductComponent extends GeneralProductAddComponent<LoanProduct> implements AfterViewInit, OnInit {

  addLoanProductForm = this.formBuilder.group(
    Object.assign(Object.assign({}, new LoanProduct()), {
      productAvailabilityModeInfo: new FormControl<string[]>([]),
      productAvailabilities: new FormControl<ProductAvailability[]>([]),
      newAccountSetting: this.formBuilder.group(new ProductNewAccountSetting()),
      productFees: new FormControl<ProductFee[]>([]),
      currencies: new FormControl<string[]>([])
    })
  );

  protected override getProductCategoryType(): ProductCategoryType {
    return ProductCategoryType.LOAN;
  }
  protected override getServiceUrl(): string {
    return environment.apiUrl.loanProduct;
  }
  protected override getAddForm(): FormGroup<any> {
    return this.addLoanProductForm;
  }
  protected override newEmptyEntity(): LoanProduct {
    return new LoanProduct();
  }
}
