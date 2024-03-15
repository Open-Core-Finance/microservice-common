import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormBuilder, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { Subscription } from 'rxjs';
import { ValueConstraint } from 'src/app/classes/products/ValueConstraint';
import { LanguageService } from 'src/app/services/language.service';
import { CurrencyService } from 'src/app/services/currency.service';

@Component({
  selector: 'app-value-constraints-input',
  templateUrl: './value-constraints-input.component.html',
  styleUrl: './value-constraints-input.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => ValueConstraintsInputComponent),
      multi: true
    }
  ]
})
export class ValueConstraintsInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  lastSupportedCurrencies: string[] | undefined;
  currencies: Currency[] = [];
  currenciesToDisplay: Currency[] = [];
  currenciesSubscription: Subscription | undefined;
  @Input()
  labelKey: string = "";
  @Input()
  labelKeyMinVal = "";
  @Input()
  labelKeyMaxVal = "";
  @Input()
  labelKeyDefaultVal = "";
  _value: ValueConstraint[] = [];

  public constructor(public languageService: LanguageService, private currencyService: CurrencyService, protected formBuilder: FormBuilder) {
    this.currenciesSubscription?.unsubscribe();
    this.currenciesSubscription = this.currencyService.currenciesSubject.subscribe( c => {
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

  writeValue(value: ValueConstraint[]): void {
    this._value = value;
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

  propagateChange = (_: ValueConstraint[]) => { };
  propagateTouched = (_: ValueConstraint[]) => { };

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

  @Input()
  set value(value: ValueConstraint[]) {
    this.writeValue(value);
  }

  get value() {
    return this._value;
  }
}
