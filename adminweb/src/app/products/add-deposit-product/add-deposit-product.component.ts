import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {environment} from "../../../environments/environment";
import {DepositProduct} from "../../classes/products/DepositProduct";
import { ProductAvailability, ProductFee, ProductNewAccountSetting} from "../../classes/products/Product";
import {CurrencyLimitValue} from "../../classes/products/ValueConstraint";
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
    const formValue = this.addDepositProductForm.value;
    if (formValue.currencies) {
      for (let i = 0; i < this.currencies.length; i++) {
        const c = this.currencies[i];
        for (let currencyId of formValue.currencies) {
          if (currencyId == c.id) {
            this.checkCurrencyChanged(c);
            break;
          }
        }
      }
    }
  }

  private checkCurrencyChanged(currency: Currency) {
    const formValue = this.addDepositProductForm.value;
    let found = false;
    let overdraftsLimits: CurrencyLimitValue[] = formValue.maxOverdraftLimit || [];
    for (const limits of overdraftsLimits) {
      if (limits.currencyId == currency.id) {
        found = true;
        break;
      }
    }
    if (!found) {
      var currencyValue = new CurrencyLimitValue();
      currencyValue.currencyId = currency.id;
      currencyValue.currencyName = currency.name;
      overdraftsLimits.push(currencyValue);
    }
    this.addDepositProductForm.controls.maxOverdraftLimit.setValue(overdraftsLimits);
  }
}
