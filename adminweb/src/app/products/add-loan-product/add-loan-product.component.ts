import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {environment} from "../../../environments/environment";
import { ProductAvailability, ProductFee, ProductNewAccountSetting} from "../../classes/products/Product";
import {ProductCategoryType} from "../../classes/products/ProductCategory";
import { GeneralProductAddComponent } from '../GeneralProductAddComponent';
import { LoanProduct } from 'src/app/classes/products/LoanProduct';

@Component({
  selector: 'app-add-loan-product',
  templateUrl: './add-loan-product.component.html',
  styleUrl: './add-loan-product.component.sass'
})
export class AddLoanProductComponent extends GeneralProductAddComponent<LoanProduct> implements AfterViewInit, OnInit {

  addProductForm = this.formBuilder.group(
    Object.assign(Object.assign({}, new LoanProduct()), {
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
    return this.addProductForm;
  }
  protected override newEmptyEntity(): LoanProduct {
    return new LoanProduct();
  }

  ngAfterViewInit(): void {
  }
}
