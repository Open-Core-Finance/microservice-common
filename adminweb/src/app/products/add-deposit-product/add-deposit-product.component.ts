import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {environment} from "../../../environments/environment";
import {DepositProduct} from "../../classes/products/DepositProduct";
import { ProductAvailability, ProductFee, ProductNewAccountSetting} from "../../classes/products/Product";
import {CurrencyLimitValue, ValueConstraint} from "../../classes/products/ValueConstraint";
import {WithdrawalLimit} from "../../classes/products/WithdrawalLimit";
import {ProductCategoryType} from "../../classes/products/ProductCategory";
import { DepositLimit } from 'src/app/classes/products/DepositLimit';
import { GeneralProductAddComponent } from '../GeneralProductAddComponent';
import { CreditArrangementManaged } from 'src/app/classes/products/CreditArrangementManaged';
import { Currency } from 'src/app/classes/Currency';
import { FrequencyOptionYearly } from 'src/app/classes/products/FrequencyOption';

@Component({
  selector: 'app-add-deposit-product',
  templateUrl: './add-deposit-product.component.html',
  styleUrl: './add-deposit-product.component.sass'
})
export class AddDepositProductComponent extends GeneralProductAddComponent<DepositProduct> implements AfterViewInit, OnInit {
  creditArrangementManagedEnum = CreditArrangementManaged;
  allTermsUnit = Object.keys(FrequencyOptionYearly);
  addDepositProductForm = this.formBuilder.group(
    Object.assign(Object.assign({}, new DepositProduct()), {
      productAvailabilityModeInfo: new FormControl<string[]>([]),
      productAvailabilities: new FormControl<ProductAvailability[]>([]),
      newAccountSetting: this.formBuilder.group(new ProductNewAccountSetting()),
      productFees: new FormControl<ProductFee[]>([]),
      depositLimits: new FormControl<DepositLimit[]>([]),
      withdrawalLimits: new FormControl<WithdrawalLimit[]>([]),
      currencies: new FormControl<string[]>([]),
      maxOverdraftLimit: new FormControl<CurrencyLimitValue[]>([])
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

  hasCurrenciesSelected(): boolean {
    var currencies = this.addDepositProductForm.value.currencies;
    return currencies != null && currencies.length > 0;
  }

  protected override currenciesChanged(): void {
    super.currenciesChanged();
    if (this.addDepositProductForm.value.maxOverdraftLimit) {
      const maxOverdraftLimit = this.addDepositProductForm.value.maxOverdraftLimit;
      this.cleanUpConstraints(maxOverdraftLimit);
      this.addMissingConstraints(maxOverdraftLimit, new CurrencyLimitValue());
    }
  }

}
