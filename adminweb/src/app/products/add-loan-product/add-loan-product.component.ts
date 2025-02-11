import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {environment} from "../../../environments/environment";
import {ProductCategoryType} from "../../classes/products/ProductCategory";
import { GeneralProductAddComponent } from '../GeneralProductAddComponent';
import { LoanProduct, LoanProductFee } from 'src/app/classes/products/LoanProduct';
import { CurrencyLimitValue, ValueConstraint } from 'src/app/classes/products/ValueConstraint';
import { CreditArrangementManaged } from 'src/app/classes/products/CreditArrangementManaged';
import { PenaltyCalculationMethod } from 'src/app/classes/products/PenaltySetting';
import { ArrearsDaysCalculatedFrom } from 'src/app/classes/products/ArrearsSetting';
import { ExpansionPanelInputGroup, UiFormCheckbox, UiFormComplexInput, UiFormInput, UiFormItem, UiFormSelect, UiSelectItem } from 'src/app/classes/ui/UiFormInput';

@Component({
    selector: 'app-add-loan-product',
    templateUrl: './add-loan-product.component.html',
    styleUrl: './add-loan-product.component.sass',
    standalone: false
})
export class AddLoanProductComponent extends GeneralProductAddComponent<LoanProduct> implements AfterViewInit, OnInit {

  creditArrangementManagedEnum = CreditArrangementManaged;
  penaltyCalculationMethodEnum = PenaltyCalculationMethod;
  allPenaltyCalculationMethods = Object.keys(PenaltyCalculationMethod);
  arrearsDaysCalculatedFromEnum = ArrearsDaysCalculatedFrom;
  allArrearsDaysCalculatedFroms = Object.keys(ArrearsDaysCalculatedFrom);

  protected override getProductCategoryType(): ProductCategoryType {
    return ProductCategoryType.LOAN;
  }
  protected override getServiceUrl(): string {
    return environment.apiUrl.loanProduct;
  }

  protected override newEmptyEntity(): LoanProduct {
    return new LoanProduct();
  }

  protected override currenciesChanged() {
    super.currenciesChanged();
    if ((this.addForm.value as any).loanValues) {
      const loanValues = (this.addForm.value as any).loanValues;
      this.cleanUpConstraints(loanValues);
      this.addMissingConstraints(loanValues, new ValueConstraint(), true);
    }
    const pennaltyForm = (this.addForm.controls as any).penaltySetting;
    if (pennaltyForm.value.penaltyRateConstraints) {
      const penaltyConstraints: ValueConstraint[] = pennaltyForm.value.penaltyRateConstraints;
      this.cleanUpConstraints(penaltyConstraints);
      this.addMissingConstraints(penaltyConstraints, new ValueConstraint(), false);
    }
    const arrearsSettingForm = (this.addForm.controls as any).arrearsSetting;
    if (arrearsSettingForm.value.toleranceAmounts) {
      const toleranceAmounts: ValueConstraint[] = arrearsSettingForm.value.toleranceAmounts;
      this.cleanUpConstraints(toleranceAmounts);
      this.addMissingConstraints(toleranceAmounts, new ValueConstraint(), false);
    }
    if (arrearsSettingForm.value.tolerancePeriods) {
      const tolerancePeriods: ValueConstraint[] = arrearsSettingForm.value.tolerancePeriods;
      this.cleanUpConstraints(tolerancePeriods);
      this.addMissingConstraints(tolerancePeriods, new ValueConstraint(), false);
    }
    if (arrearsSettingForm.value.floors) {
      const floors = arrearsSettingForm.value.floors;
      this.cleanUpConstraints(floors);
      this.addMissingConstraints(floors, new CurrencyLimitValue(), true);
    }
  }
  
  protected override get additionalFormGroupElement(): any {
    const result = super.additionalFormGroupElement;
    result.productFees = new FormControl<LoanProductFee[]>([]);
    result.loanValues = new FormControl<ValueConstraint[]>([]);
    result.penaltySetting = new FormGroup({
      sameConstraintForAllCurrency: new FormControl(false),
      calculationMethod: new FormControl(PenaltyCalculationMethod.NONE),
      penaltyTolerancePeriod: new FormControl(1),
      penaltyRateConstraints: new FormControl<ValueConstraint[]>([])
    });
    result.arrearsSetting = new FormGroup({
      sameConstraintForAllCurrency: new FormControl(false),
      includeNonWorkingDay: new FormControl(false),
      daysCalculatedFrom: new FormControl(ArrearsDaysCalculatedFrom.OLDEST_LATE_REPAYMENT),
      tolerancePeriods: new FormControl<ValueConstraint[]>([]),
      toleranceAmounts: new FormControl<ValueConstraint[]>([]),
      floors: new FormControl<CurrencyLimitValue[]>([]),
    })
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

    // loanAmount
    prefix = "loanProduct.";
    formItems = [];
    formItems.push(new UiFormComplexInput("constraints", "constraints"));
    formItems.push(new UiFormSelect("creditArrangementManaged.label",[
      ({selectValue: CreditArrangementManaged.OPTIONAL, labelKey: 'creditArrangementManaged.OPTIONAL'} as UiSelectItem),
      ({selectValue: CreditArrangementManaged.REQUIRED, labelKey: 'creditArrangementManaged.REQUIRED'} as UiSelectItem),
      ({selectValue: CreditArrangementManaged.NO, labelKey: 'creditArrangementManaged.NO'} as UiSelectItem)
    ], "underCreditArrangementManaged"));
    result.push(new ExpansionPanelInputGroup(prefix + 'loanAmount', formItems));

    // Loan interest rate
    formItems = [];
    formItems.push(new UiFormComplexInput("interestRate", "interestRate"));
    result.push(new ExpansionPanelInputGroup(prefix + 'interestRate.label', formItems));

    // Repayment Scheduling
    formItems = [];
    formItems.push(new UiFormComplexInput("repaymentScheduling", "repaymentScheduling"));
    result.push(new ExpansionPanelInputGroup(prefix + 'repaymentScheduling', formItems));

    // Repayment Collection
    formItems = [];
    formItems.push(new UiFormComplexInput("repaymentCollection", "repaymentCollection"));
    result.push(new ExpansionPanelInputGroup(prefix + 'repaymentCollection', formItems));

    // Arrears Setting
    formItems = [];
    formItems.push(new UiFormComplexInput("arrearsSetting", "arrearsSetting"));
    result.push( new ExpansionPanelInputGroup(prefix + 'arrearsSetting', formItems));

    // Penalty Setting
    formItems = [];
    formItems.push(new UiFormComplexInput("penaltySetting", "penaltySetting"));
    result.push( new ExpansionPanelInputGroup(prefix + 'penaltySetting', formItems));

    // Internal control
    formItems = [];
    formItems.push(new UiFormCheckbox(prefix + "closeDormantAccounts", "closeDormantAccounts"));
    formItems.push(new UiFormCheckbox(prefix + "lockArrearsAccounts", "lockArrearsAccounts"));
    formItems.push(new UiFormCheckbox(prefix + "capCharges", "capCharges"));
    formItems.push(new UiFormCheckbox(prefix + "enableGuarantors", "enableGuarantors"));
    formItems.push(new UiFormCheckbox(prefix + "enableCollateral", "enableCollateral"));
    formItem = new UiFormInput(prefix + "requiredSecurity", "percentSecurityPerLoan");
    formItem.visibleFn = () => (that.addForm.value as any).enableCollateral || (that.addForm.value as any).enableGuarantors;
    (formItem as UiFormInput).postFixLabelKey = prefix + "percentLoanAmount";
    formItems.push(formItem);
    result.push( new ExpansionPanelInputGroup(prefix + 'internalControl', formItems));

    // Return
    return result;
  }
}
