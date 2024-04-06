import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormControl} from "@angular/forms";
import {environment} from "../../../environments/environment";
import {DepositProductFee} from "../../classes/products/DepositProduct";
import {CurrencyLimitValue} from "../../classes/products/ValueConstraint";
import {WithdrawalLimit} from "../../classes/products/WithdrawalLimit";
import {ProductCategoryType} from "../../classes/products/ProductCategory";
import { DepositLimit } from 'src/app/classes/products/DepositLimit';
import { GeneralProductAddComponent } from '../GeneralProductAddComponent';
import { CreditArrangementManaged } from 'src/app/classes/products/CreditArrangementManaged';
import { FrequencyOptionYearly } from 'src/app/classes/products/FrequencyOption';
import { CryptoProduct } from 'src/app/classes/products/CryptoProduct';
import { ExpansionPanelInputGroup, UiFormCheckbox, UiFormComplexInput, UiFormInput, UiFormItem } from 'src/app/classes/ui/UiFormInput';

@Component({
  selector: 'app-add-crypto-product',
  templateUrl: './add-crypto-product.component.html',
  styleUrl: './add-crypto-product.component.sass'
})
export class AddCryptoProductComponent extends GeneralProductAddComponent<CryptoProduct> implements AfterViewInit, OnInit {

  creditArrangementManagedEnum = CreditArrangementManaged;
  allTermsUnit = Object.keys(FrequencyOptionYearly);
  
  protected override getProductCategoryType(): ProductCategoryType {
    return ProductCategoryType.CRYPTO;
  }
  protected override getServiceUrl(): string {
    return environment.apiUrl.cryptoProduct;
  }

  protected override newEmptyEntity(): CryptoProduct {
    return new CryptoProduct();
  }

  protected override currenciesChanged(): void {
    super.currenciesChanged();
    if (this.addForm.value.maxOverdraftLimit) {
      const maxOverdraftLimit = this.addForm.value.maxOverdraftLimit;
      this.cleanUpConstraints(maxOverdraftLimit);
      this.addMissingConstraints(maxOverdraftLimit, new CurrencyLimitValue(), true);
    }
  }

  protected override get additionalFormGroupElement(): any {
    const result = super.additionalFormGroupElement;
    result.productFees = new FormControl<DepositProductFee[]>([]);
    result.depositLimits = new FormControl<DepositLimit[]>([]);
    result.withdrawalLimits = new FormControl<WithdrawalLimit[]>([]);
    result.maxOverdraftLimit = new FormControl<CurrencyLimitValue[]>([]);
    return result;
  }

  protected override buildFormInputGroups(): ExpansionPanelInputGroup[] {
    var that = this;
    const result = super.buildFormInputGroups();
    let prefix = "product.";

    // Product fees
    let formItems: UiFormItem[] = [];
    let formItem: UiFormItem = new UiFormCheckbox(prefix + "allowArbitraryFees", "allowArbitraryFees");
    formItem.visibleFn = () => that.currenciesToDisplay.length > 1;
    formItems.push(formItem);
    formItems.push(new UiFormComplexInput("productFees", "productFees"));
    result.push(new ExpansionPanelInputGroup(prefix + 'productFees', formItems));

    // Internal control
    prefix = "depositProduct.";
    formItems = [];
    formItems.push(new UiFormCheckbox(prefix + "earlyClosurePeriod", "enableEarlyClosurePeriod"));

    formItem = new UiFormInput(prefix + "earlyClosurePeriodVal", "earlyClosurePeriod");
    formItem.visibleFn = () => (this.addForm.value as any).enableEarlyClosurePeriod;
    formItems.push(formItem);

    formItems.push(new UiFormCheckbox(prefix + "autoDormantAccount", "autoSetAsDormant"));
    formItem = new UiFormInput(prefix + "domainAfterLabel", "daysToSetToDormant");
    formItem.visibleFn = () => (this.addForm.value as any).autoSetAsDormant;
    (formItem as UiFormInput).postFixLabelKey = prefix + "domainDaysLabel";
    formItems.push(formItem);

    result.push(new ExpansionPanelInputGroup(prefix + 'internalControl', formItems));

    // Deposit interest rate
    formItems = [];
    formItems.push(new UiFormCheckbox(prefix + "interestRate.enableInterestRate", "enableInterestRate"));
    formItems.push(new UiFormComplexInput("interestRate", "interestRate"));
    result.push(new ExpansionPanelInputGroup(prefix + 'interestRate.label', formItems));

    // Overdrafts
    formItems = [];
    formItems.push(new UiFormCheckbox(prefix + "allowOverdrafts", "allowOverdrafts"));
    formItems.push(new UiFormComplexInput("overdrafts", "overdrafts"));
    result.push(new ExpansionPanelInputGroup(prefix + 'overdrafts', formItems));

    // Fixed Deposit
    formItems = [];
    formItems.push(new UiFormCheckbox(prefix + "enableTermDeposit", "enableTermDeposit"));
    formItems.push(new UiFormComplexInput("termDeposit", "termDeposit"));
    result.push(new ExpansionPanelInputGroup(prefix + 'fixedDeposit', formItems));

    // Deposit Limit
    formItems = [];
    formItems.push(new UiFormComplexInput("depositLimits", "depositLimits"));
    result.push(new ExpansionPanelInputGroup(prefix + 'depositLimits', formItems));

    // Withdrawal Limit
    formItems = [];
    formItems.push(new UiFormComplexInput("withdrawalLimit", "withdrawalLimit"));
    result.push(new ExpansionPanelInputGroup(prefix + 'withdrawalLimit', formItems));

    // Return
    return result;
  }
}
