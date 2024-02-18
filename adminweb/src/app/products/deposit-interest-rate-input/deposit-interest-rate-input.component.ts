import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { DepositBalanceInterestCalculation, DepositInterestRate, DepositInterestRateTerms, InterestCalculationDateOptionType } from 'src/app/classes/products/DepositProduct';
import { Subscription } from 'rxjs';
import { LanguageService } from 'src/app/services/language.service';
import { EntitiesService } from 'src/app/services/EntitiesService';
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
  ]
})
export class DepositInterestRateInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  lastSupportedCurrencies: string[] | undefined;
  currencies: Currency[] = [];
  currenciesToDisplay: Currency[] = [];
  currenciesSubscription: Subscription | undefined;
  value: DepositInterestRate = new DepositInterestRate();
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

  public constructor(public languageService: LanguageService, private entityService: EntitiesService) {
    this.currenciesSubscription?.unsubscribe();
    this.currenciesSubscription = this.entityService.entitySubjectMap.get(EntitiesService.ENTITY_TYPE_CURRENCY)?.subscribe( c => {
      this.currencies = c;
      if (this.lastSupportedCurrencies) {
        this.populateCurrenciesToUi(this.lastSupportedCurrencies);
      }
    });
  }

  ngOnDestroy(): void {
    this.currenciesSubscription?.unsubscribe();
  }

  ngOnInit(): void {
  }

  writeValue(value: DepositInterestRate): void {
    this.value = value;
    this.populateCurrenciesToUi(this.lastSupportedCurrencies ? this.lastSupportedCurrencies : []);
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

  propagateChange = (_: DepositInterestRate[]) => { };
  propagateTouched = (_: DepositInterestRate[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: string[]) {
    this.lastSupportedCurrencies = supportedCurrencies;
    this.populateCurrenciesToUi(supportedCurrencies);
  }

  private populateCurrenciesToUi(supportedCurrencies: string[]) {
    this.currenciesToDisplay = [];
    if (supportedCurrencies.length > 0) {
      this.currencies.forEach((value, index, arr) => {
        for(let  i = 0; i < supportedCurrencies.length; i++) {
          const c = supportedCurrencies[i];
          if (c == value.id) {
            this.currenciesToDisplay.push(value);
            this.checkAndUpdateCurrency(value);
            break;
          }
        }
      });
    } else {
      this.value.interestRateConstraints = [];
      this.value.interestItems = [];
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
      item.currencyName = currency.name;
      this.value.interestRateConstraints.push(item);
    }
    if (!this.lastSupportedCurrencies || this.lastSupportedCurrencies.length < 1) {
      this.value.interestRateConstraints = [];
      this.value.interestItems = [];
    } else {
      // Clean remove currencies constraint
      for(let  i = 0; i < constraints.length; i++) {
        var c = constraints[i];
        found = false;
        for (const supportedCurrency of this.lastSupportedCurrencies) {
          if (supportedCurrency == c.currencyId) {
            found = true;
            break;
          }
        }
        if (!found) {
          constraints.splice(i, 1);
          i--;
        }
      }
      // Check tier
      if (this.value.interestItems.length < 1) {
        var item = new TieredInterestItem();
        item.currencyId = currency.id;
        item.currencyName = currency.name;
        this.value.interestItems.push(item);
      }
    }
  }

  addTierClick($event: MouseEvent) {
    if (this.currenciesToDisplay.length > 0) {
      var currency = this.currenciesToDisplay[0];
      if (this.value.interestItems.length > 0) {
        const last = this.value.interestItems[this.value.interestItems.length - 1];
        for (const c of this.currenciesToDisplay) {
          if (c.id == last.currencyId) {
            currency = c;
            break;
          }
        }
      }
      var item = new TieredInterestItem();
      item.currencyId = currency.id;
      item.currencyName = currency.name;
      this.value.interestItems.push(item);
    }
  }

  removeTierClick($event: MouseEvent, index: number) {
    this.value.interestItems.splice(index, 1);
  }

  inerestTierCurrencyChanged($event: any, item: TieredInterestItem) {
    for (const currency of this.currenciesToDisplay) {
      if ($event == currency.id) {
        item.currencyName = currency.name;
        break;
      }
    }
  }
}