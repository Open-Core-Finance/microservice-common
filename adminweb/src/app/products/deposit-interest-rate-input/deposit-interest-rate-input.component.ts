import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormBuilder, FormControl, FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { DepositBalanceInterestCalculation, DepositInterestRate, DepositInterestRateTerms, InterestCalculationDateOptionType } from 'src/app/classes/products/DepositProduct';
import { LanguageService } from 'src/app/services/language.service';
import { InterestCalculationMethod } from 'src/app/classes/products/InterestCalculationMethod';
import { InterestDayInYear } from 'src/app/classes/products/InterestDayInYear';
import { ValueConstraint } from 'src/app/classes/products/ValueConstraint';
import { TieredInterestItem } from 'src/app/classes/products/TieredInterestItem';

@Component({
    selector: 'app-deposit-interest-rate-input',
    templateUrl: './deposit-interest-rate-input.component.html',
    styleUrl: './deposit-interest-rate-input.component.sass',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => DepositInterestRateInputComponent),
            multi: true
        }
    ],
    standalone: false
})
export class DepositInterestRateInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  _supportedCurrencies: Currency[] = [];
  value: DepositInterestRate = new DepositInterestRate();
  childForm: FormGroup;
  depositInterestRateTermsEnum = DepositInterestRateTerms;
  allTerms = Object.keys(DepositInterestRateTerms);
  interestCalculationMethodEnum = InterestCalculationMethod;
  allCalculationMethods = Object.keys(InterestCalculationMethod);
  balanceInterestCalculationEnum = DepositBalanceInterestCalculation;
  allBalanceCalculations = Object.keys(DepositBalanceInterestCalculation);
  calculationDateOptionTypeEnum = InterestCalculationDateOptionType;
  allDateOptionTypes = Object.keys(InterestCalculationDateOptionType);
  interestDayInYearEnum = InterestDayInYear;
  allDayInYearOption = Object.keys(InterestDayInYear);

  public constructor(public languageService: LanguageService, protected formBuilder: FormBuilder) {
    this.childForm = this.buildChildForm();
  }

  private buildChildForm(): FormGroup {
    return this.formBuilder.group({
      interestItems: new FormControl<TieredInterestItem[]>([])
    });
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  writeValue(value: DepositInterestRate): void {
    this.value = value;
    this.populateCurrenciesToUi(this._supportedCurrencies);
  }
 
  registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.propagateTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }

  propagateChange = (_: DepositInterestRate) => { };
  propagateTouched = (_: DepositInterestRate) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: Currency[]) {
    this._supportedCurrencies = supportedCurrencies;
    this.populateCurrenciesToUi(supportedCurrencies);
  }

  get supportedCurrencies(): Currency[] {
    return this._supportedCurrencies;
  }

  private populateCurrenciesToUi(supportedCurrencies: Currency[]) {
    if (supportedCurrencies.length > 0) {
      let changed = false;
      for(let  i = 0; i < supportedCurrencies.length; i++) {
        if (this.checkAndUpdateCurrency(supportedCurrencies[i])) {
          changed = true;
        }
      }
      if (changed) {
        this.propagateChange(this.value);
      }
    } else {
      this.value.interestRateConstraints = [];
      this.propagateChange(this.value);
    }
  }

  private checkAndUpdateCurrency(currency: Currency): boolean {
    let found = false;
    let changed = false;
    var constraints = this.value.interestRateConstraints;
    for (const currencyValue of constraints) {
      if (currencyValue.currencyId == currency.id) {
        found = true;
        break;
      }
    }
    if (!found) {
      const item = new ValueConstraint();
      item.currencyId = currency.id;
      this.value.interestRateConstraints.push(item);
      changed = true;
    }
    if (this._supportedCurrencies.length < 1) {
      this.value.interestRateConstraints = [];
      changed = true;
    } else {
      // Clean remove currencies constraint
      for(let  i = 0; i < constraints.length; i++) {
        var c = constraints[i];
        found = false;
        for (const supportedCurrency of this._supportedCurrencies) {
          if (supportedCurrency.id == c.currencyId) {
            found = true;
            break;
          }
        }
        if (!found) {
          constraints.splice(i, 1);
          i--;
          changed = true;
        }
      }
    }
    return changed;
  }

  tieredInterestSubComponentChanged() {
    this.value.interestItems = this.childForm.controls['interestItems'].value;
    this.propagateChange(this.value);
  }
}