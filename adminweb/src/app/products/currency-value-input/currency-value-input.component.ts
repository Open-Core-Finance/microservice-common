import { Component, OnInit, forwardRef, OnDestroy, Input } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Subscription } from 'rxjs';
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
  lastSupportedCurrencies: string[] | undefined;
  currencies: Currency[] = [];
  currenciesSubscription: Subscription | undefined;
  values: CurrencyLimitValue[] = [];

  public constructor(public languageService: LanguageService, private currencyService: CurrencyService) {
  }

  ngOnDestroy(): void {
    this.currenciesSubscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.currenciesSubscription?.unsubscribe();
    this.currenciesSubscription = this.currencyService.currenciesSubject.subscribe( c => {
      this.currencies = c;
      if (this.lastSupportedCurrencies) {
        this.populateCurrenciesToUi(this.lastSupportedCurrencies);
      }
    });
  }

  writeValue(value: CurrencyLimitValue[]): void {
    this.values = value;
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

  propagateChange = (_: CurrencyLimitValue[]) => { };
  propagateTouched = (_: CurrencyLimitValue[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: string[]) {
    this.lastSupportedCurrencies = supportedCurrencies;
    this.populateCurrenciesToUi(supportedCurrencies);
  }

  private populateCurrenciesToUi(supportedCurrencies: string[]) {
    this.currencies.forEach((value, index, arr) => {
      for(let  i = 0; i < supportedCurrencies.length; i++) {
        const c = supportedCurrencies[i];
        if (c == value.id) {
          this.checkAndAddCurrencyLimit(value);
          break;
        }
      }
    });
  }

  private checkAndAddCurrencyLimit(currency: Currency) {
    let found = false;
    for (const currencyValue of this.values) {
      if (currencyValue.currencyId == currency.id) {
        found = true;
        break;
      }
    }
    if (!found) {
      var currencyValue = new CurrencyLimitValue();
      currencyValue.currencyId = currency.id;
      this.values.push(currencyValue);
    }
  }
  
}
