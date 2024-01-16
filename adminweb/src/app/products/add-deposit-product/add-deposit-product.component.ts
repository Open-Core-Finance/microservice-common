import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {environment} from "../../../environments/environment";
import { DepositInterestCalculationDateOption,  DepositInterestRate,  DepositProduct} from "../../classes/products/DepositProduct";
import { ProductAvailability, ProductFee, ProductNewAccountSetting} from "../../classes/products/Product";
import {ValueConstraint} from "../../classes/products/ValueConstraint";
import {TieredInterestItem} from "../../classes/products/TieredInterestItem";
import {WithdrawalLimit} from "../../classes/products/WithdrawalLimit";
import {ProductCategoryType} from "../../classes/products/ProductCategory";
import { DepositLimit } from 'src/app/classes/products/DepositLimit';
import { GeneralProductAddComponent } from '../GeneralProductAddComponent';

@Component({
  selector: 'app-add-deposit-product',
  templateUrl: './add-deposit-product.component.html',
  styleUrl: './add-deposit-product.component.sass'
})
export class AddDepositProductComponent extends GeneralProductAddComponent<DepositProduct> implements AfterViewInit, OnInit {

  addDepositProductForm = this.formBuilder.group(
    Object.assign(Object.assign({}, new DepositProduct()), {
      productAvailabilityModeInfo: new FormControl<string[]>([]),
      productAvailabilities: new FormControl<ProductAvailability[]>([]),
      newAccountSetting: this.formBuilder.group(new ProductNewAccountSetting()),
      productFees: new FormControl<ProductFee[]>([]),
      interestRate: this.initDepositInterestForm(),
      depositLimits: new FormControl<DepositLimit[]>([]),
      withdrawalLimit: new FormControl(new WithdrawalLimit()),
      overdraftsInterest: this.initDepositInterestForm(),
      currencies: new FormControl<string[]>([])
    })
  );
  
  protected override getProductCategoryType(): ProductCategoryType {
    return ProductCategoryType.DEPOSIT;
  }
  protected override getServiceUrl(): string {
    return environment.apiUrl.depositProduct;
  }
  protected override getAddForm(): FormGroup<any> {
    return this.addDepositProductForm;
  }
  protected override newEmptyEntity(): DepositProduct {
    return new DepositProduct();
  }

  ngAfterViewInit(): void {
  }

  private initDepositInterestForm(): any {
    return this.formBuilder.group(
      Object.assign(Object.assign({}, new DepositInterestRate()), {
        calculationDateOption: this.formBuilder.group(new DepositInterestCalculationDateOption()),
        interestRateConstraint: this.formBuilder.group(new ValueConstraint()),
        interestItems: this.formBuilder.group(new TieredInterestItem())
      })
    );
  }
}
