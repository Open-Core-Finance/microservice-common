import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {environment} from "../../../environments/environment";
import {ProductCategoryType} from "../../classes/products/ProductCategory";
import { GeneralProductAddComponent } from '../GeneralProductAddComponent';
import { LoanProduct, LoanProductFee } from 'src/app/classes/products/LoanProduct';
import { ProductAvailability, ProductNewAccountSetting } from 'src/app/classes/products/Product';
import { CurrencyLimitValue, ValueConstraint } from 'src/app/classes/products/ValueConstraint';
import { CreditArrangementManaged } from 'src/app/classes/products/CreditArrangementManaged';
import { PenaltyCalculationMethod } from 'src/app/classes/products/PenaltySetting';
import { ArrearsDaysCalculatedFrom } from 'src/app/classes/products/ArrearsSetting';

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
      productFees: new FormControl<LoanProductFee[]>([]),
      currencies: new FormControl<string[]>([]),
      loanValues: new FormControl<ValueConstraint[]>([]),
      penaltySetting: new FormGroup({
        calculationMethod: new FormControl(PenaltyCalculationMethod.NONE),
        penaltyTolerancePeriod: new FormControl(1),
        penaltyRateConstraints: new FormControl<ValueConstraint[]>([])
      }),
      arrearsSetting: new FormGroup({
        includeNonWorkingDay: new FormControl(false),
        daysCalculatedFrom: new FormControl(ArrearsDaysCalculatedFrom.OLDEST_LATE_REPAYMENT),
        tolerancePeriods: new FormControl<ValueConstraint[]>([]),
        toleranceAmounts: new FormControl<ValueConstraint[]>([]),
        floors: new FormControl<CurrencyLimitValue[]>([]),
      })
    })
  );

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
  protected override getAddForm(): FormGroup<any> {
    return this.addLoanProductForm;
  }
  protected override newEmptyEntity(): LoanProduct {
    return new LoanProduct();
  }

  protected override currenciesChanged() {
    super.currenciesChanged();
    if (this.addLoanProductForm.value.loanValues) {
      const loanValues = this.addLoanProductForm.value.loanValues;
      this.cleanUpConstraints(loanValues);
      this.addMissingConstraints(loanValues, new ValueConstraint(), false);
    }
    const pennaltyForm = this.addLoanProductForm.controls.penaltySetting;
    if (pennaltyForm.value.penaltyRateConstraints) {
      const penaltyConstraints: ValueConstraint[] = pennaltyForm.value.penaltyRateConstraints;
      this.cleanUpConstraints(penaltyConstraints);
      this.addMissingConstraints(penaltyConstraints, new ValueConstraint(), false);
    }
    const arrearsSettingForm = this.addLoanProductForm.controls.arrearsSetting;
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
      this.addMissingConstraints(floors, new CurrencyLimitValue(), false);
    }
  }
}
