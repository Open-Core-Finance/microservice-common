import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { Subscription } from 'rxjs';
import { DepositLimit, DepositLimitType } from 'src/app/classes/products/DepositLimit';
import { LanguageService } from 'src/app/services/language.service';
import { EntitiesService } from 'src/app/services/EntitiesService';

@Component({
  selector: 'app-deposit-limit-input',
  templateUrl: './deposit-limit-input.component.html',
  styleUrl: './deposit-limit-input.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DepositLimitInputComponent),
      multi: true
    }
  ]
})
export class DepositLimitInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  lastSupportedCurrencies: string[] | undefined;
  currencies: Currency[] = [];
  currenciesToDisplay: Currency[] = [];
  currenciesSubscription: Subscription | undefined;
  value: DepositLimit[] = [];
  depositLimitTypeEnum = DepositLimitType;
  allTypes = Object.keys(DepositLimitType);

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

  writeValue(value: DepositLimit[]): void {
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

  propagateChange = (_: DepositLimit[]) => { };
  propagateTouched = (_: DepositLimit[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: string[]) {
    this.lastSupportedCurrencies = supportedCurrencies;
    this.populateCurrenciesToUi(supportedCurrencies);
  }

  private populateCurrenciesToUi(supportedCurrencies: string[]) {
    this.currenciesToDisplay = [];
    this.currencies.forEach((value, index, arr) => {
      for(let  i = 0; i < supportedCurrencies.length; i++) {
        const c = supportedCurrencies[i];
        if (c == value.id) {
          this.currenciesToDisplay.push(value);
          break;
        }
      }
    });
  }

  limitCurrencyChanged($event: any, limit: DepositLimit) {
    for (const currency of this.currenciesToDisplay) {
      if ($event == currency.id) {
        limit.currencyName = currency.name;
        break;
      }
    }
  }

  addLimitClick($event: MouseEvent) {
    if (this.currenciesToDisplay.length > 0) {
      var currency = this.currenciesToDisplay[0];
      if (this.value.length > 0) {
        const last = this.value[this.value.length - 1];
        for (const c of this.currenciesToDisplay) {
          if (c.id == last.currencyId) {
            currency = c;
            break;
          }
        }
      }
      var item = new DepositLimit();
      item.currencyId = currency.id;
      item.currencyName = currency.name;
      this.value.push(item);
    }
  }

  removeLimitClick($event: MouseEvent, index: number) {
    this.value.splice(index, 1);
  }
}
