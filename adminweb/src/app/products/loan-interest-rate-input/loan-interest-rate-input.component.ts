import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { Subscription } from 'rxjs';
import { AccruedInterestPostingFrequency, LoanInterestCalculationMethod, LoanInterestRate, LoanInterestType, RepaymentsInterestCalculation } from 'src/app/classes/products/LoanProduct';
import { LanguageService } from 'src/app/services/language.service';
import { CurrencyService } from 'src/app/services/currency.service';
import { ValueConstraint } from 'src/app/classes/products/ValueConstraint';
import { InterestDayInYear } from 'src/app/classes/products/InterestDayInYear';
import { InterestCalculationMethod } from 'src/app/classes/products/InterestCalculationMethod';

@Component({
    selector: 'app-loan-interest-rate-input',
    templateUrl: './loan-interest-rate-input.component.html',
    styleUrl: './loan-interest-rate-input.component.sass',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => LoanInterestRateInputComponent),
            multi: true
        }
    ],
    standalone: false
})
export class LoanInterestRateInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  _supportedCurrencies: Currency[] = [];
  value: LoanInterestRate = new LoanInterestRate();
  loanInterestCalculationMethodEnum = LoanInterestCalculationMethod;
  allLoanCalculationMethods = Object.keys(LoanInterestCalculationMethod);
  accruedInterestPostingFrequencyEnum = AccruedInterestPostingFrequency;
  allPostingFrequency = Object.keys(AccruedInterestPostingFrequency);
  loanInterestTypeEnum = LoanInterestType;
  allInterestTypes = Object.keys(LoanInterestType);
  interestDayInYearEnum = InterestDayInYear;
  allDayInYearOption = Object.keys(InterestDayInYear);
  interestCalculationMethodEnum = InterestCalculationMethod;
  allCalculationMethods = Object.keys(InterestCalculationMethod);
  repaymentsInterestCalculationEnum = RepaymentsInterestCalculation;
  allRepaymentsInterestCalculations = Object.keys(RepaymentsInterestCalculation);

  public constructor(public languageService: LanguageService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  writeValue(value: LoanInterestRate): void {
    this.value = value;
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

  propagateChange = (_: LoanInterestRate[]) => { };
  propagateTouched = (_: LoanInterestRate[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: Currency[]) {
    this._supportedCurrencies = supportedCurrencies;
    this.populateCurrenciesToUi(supportedCurrencies);
  }

  private populateCurrenciesToUi(supportedCurrencies: Currency[]) {
    if (supportedCurrencies.length > 0) {
      for(let  i = 0; i < supportedCurrencies.length; i++) {
        this.checkAndUpdateCurrency(supportedCurrencies[i]);
      }
    } else {
      this.value.interestRateConstraints = [];
    }
  }

  private checkAndUpdateCurrency(currency: Currency) {
    let found = false;
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
    }
    if (!this._supportedCurrencies || this._supportedCurrencies.length < 1) {
      this.value.interestRateConstraints = [];
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
        }
      }
    }
  }
}
