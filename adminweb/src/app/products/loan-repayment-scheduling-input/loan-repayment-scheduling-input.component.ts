import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Currency } from 'src/app/classes/Currency';
import { FrequencyOption } from 'src/app/classes/products/FrequencyOption';
import { GracePeriodType, NonWorkingDaysRescheduling, RepaymentCurrencyRounding, RepaymentScheduleRounding, RepaymentScheduling, RepaymentSchedulingMethod, ShortMonthHandling } from 'src/app/classes/products/Repayment';
import { ValueConstraint } from 'src/app/classes/products/ValueConstraint';
import { EntitiesService } from 'src/app/services/EntitiesService';
import { LanguageService } from 'src/app/services/language.service';

@Component({
  selector: 'app-loan-repayment-scheduling-input',
  templateUrl: './loan-repayment-scheduling-input.component.html',
  styleUrl: './loan-repayment-scheduling-input.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => LoanRepaymentSchedulingInputComponent),
      multi: true
    }
  ]
})
export class LoanRepaymentSchedulingInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  lastSupportedCurrencies: string[] | undefined;
  currencies: Currency[] = [];
  currenciesToDisplay: Currency[] = [];
  currenciesSubscription: Subscription | undefined;
  value: RepaymentScheduling = new RepaymentScheduling();
  frequencyOptionEnum = FrequencyOption;
  allFrequencyOptions = Object.keys(FrequencyOption);
  repaymentSchedulingMethodEnum = RepaymentSchedulingMethod;
  allRepaymentSchedulingMethods = Object.keys(RepaymentSchedulingMethod);
  shortMonthHandlingEnum = ShortMonthHandling;
  allShortMonthHandlings = Object.keys(ShortMonthHandling);
  gracePeriodTypeEnum = GracePeriodType;
  allGracePeriodTypes = Object.keys(GracePeriodType);
  repaymentScheduleRoundingEnum = RepaymentScheduleRounding;
  allRepaymentScheduleRoundings = Object.keys(RepaymentScheduleRounding);
  repaymentCurrencyRounding = RepaymentCurrencyRounding;
  allRepaymentCurrencyRoundings = Object.keys(RepaymentCurrencyRounding);
  nonWorkingDaysReschedulingEnum = NonWorkingDaysRescheduling;
  allNonWorkingDaysReschedulings = Object.keys(NonWorkingDaysRescheduling);

  public constructor(public languageService: LanguageService, private entityService: EntitiesService) {
    this.currenciesSubscription?.unsubscribe();
    this.currenciesSubscription = this.entityService.organizationObservableMap.get(EntitiesService.ENTITY_TYPE_CURRENCY)?.subscribe( c => {
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

  writeValue(value: RepaymentScheduling): void {
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

  propagateChange = (_: RepaymentScheduling[]) => { };
  propagateTouched = (_: RepaymentScheduling[]) => { };

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
      this.value.installmentsConstraints = [];
      this.value.firstDueDateOffsetConstraints = [];
    }
  }

  private checkAndUpdateCurrency(currency: Currency) {
    this.checkConstraints(this.value.installmentsConstraints, currency);
    this.checkConstraints(this.value.firstDueDateOffsetConstraints, currency);
  }

  private checkConstraints(constraints: ValueConstraint[], currency: Currency) {
    let found = false;
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
      constraints.push(item);
    }
    if (!this.lastSupportedCurrencies || this.lastSupportedCurrencies.length < 1) {
      constraints.splice(0, constraints.length);
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
    }
  }

  fixedPaymentDayInMonthAdd() {
    this.value.repaymentDays.push(0);
  }

  fixedPaymentDayInMonthRemove(index: number) {
    this.value.repaymentDays.splice(index, 1);
  }
}
