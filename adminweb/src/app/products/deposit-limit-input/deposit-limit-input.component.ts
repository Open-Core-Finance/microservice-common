import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { DepositLimit, DepositLimitType } from 'src/app/classes/products/DepositLimit';
import { LanguageService } from 'src/app/services/language.service';
import { CurrencyService } from 'src/app/services/currency.service';

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
    ],
    standalone: false
})
export class DepositLimitInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  _supportedCurrencies: Currency[] = [];
  value: DepositLimit[] = [];
  depositLimitTypeEnum = DepositLimitType;
  allTypes = Object.keys(DepositLimitType);

  public constructor(public languageService: LanguageService, private currencyService: CurrencyService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  writeValue(value: DepositLimit[]): void {
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

  propagateChange = (_: DepositLimit[]) => { };
  propagateTouched = (_: DepositLimit[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: Currency[]) {
    this._supportedCurrencies = supportedCurrencies;
  }

  get supportedCurrencies(): Currency[] {
    return this._supportedCurrencies;
  }

  limitCurrencyChanged($event: any, limit: DepositLimit) {
  }

  addLimitClick($event: MouseEvent) {
    if (this._supportedCurrencies.length > 0) {
      var currency = this._supportedCurrencies[0];
      if (this._supportedCurrencies.length > 1) {
        currency = this._supportedCurrencies[1];
      }
      if (this.value.length > 0) {
        const last = this.value[this.value.length - 1];
        for (const c of this._supportedCurrencies) {
          if (c.id == last.currencyId) {
            currency = c;
            break;
          }
        }
      }
      var item = new DepositLimit();
      item.currencyId = currency.id;
      this.value.push(item);
    }
  }

  removeLimitClick($event: MouseEvent, index: number) {
    this.value.splice(index, 1);
  }
}
