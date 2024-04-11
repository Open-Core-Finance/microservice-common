import { Component, OnInit, forwardRef, OnDestroy, Input } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { CurrencyLimitValue } from 'src/app/classes/products/ValueConstraint';
import { CurrencyService } from 'src/app/services/currency.service';
import { LanguageService } from 'src/app/services/language.service';

@Component({
  selector: 'app-currency-value-input',
  templateUrl: './currency-value-input.component.html',
  styleUrl: './currency-value-input.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CurrencyValueInputComponent),
      multi: true
    }
  ]
})
export class CurrencyValueInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  _supportedCurrencies: Currency[] = [];
  values: CurrencyLimitValue[] = [];

  public constructor(public languageService: LanguageService, private currencyService: CurrencyService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  writeValue(value: CurrencyLimitValue[]): void {
    this.values = value;
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

  propagateChange = (_: CurrencyLimitValue[]) => { };
  propagateTouched = (_: CurrencyLimitValue[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: Currency[]) {
    this._supportedCurrencies = supportedCurrencies;
    this.populateCurrenciesToUi(this._supportedCurrencies);
  }

  get supportedCurrencies(): Currency[] {
    return this._supportedCurrencies;
  }

  private populateCurrenciesToUi(supportedCurrencies: Currency[]) {
    for(let  i = 0; i < supportedCurrencies.length; i++) {
      this.checkAndAddCurrencyLimit(supportedCurrencies[i]);
    }
  }

  private checkAndAddCurrencyLimit(currency: Currency) {
    let found = false;
    if (currency.id == 'ALL') {
      found = true;
    } else {
      for (const currencyValue of this.values) {
        if (currencyValue.currencyId == currency.id) {
          found = true;
          break;
        }
      }
    }
    if (!found) {
      var currencyValue = new CurrencyLimitValue();
      currencyValue.currencyId = currency.id;
      this.values.push(currencyValue);
    }
  }
  
}
