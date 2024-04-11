import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { DepositInterestRateTerms } from 'src/app/classes/products/DepositProduct';
import { InterestCalculationMethod } from 'src/app/classes/products/InterestCalculationMethod';
import { TieredInterestItem } from 'src/app/classes/products/TieredInterestItem';
import { LanguageService } from 'src/app/services/language.service';

@Component({
  selector: 'app-tiered-interest-input',
  templateUrl: './tiered-interest-input.component.html',
  styleUrl: './tiered-interest-input.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => TieredInterestInputComponent),
      multi: true
    }
  ]
})
export class TieredInterestInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  _value: TieredInterestItem[] = [];
  isDisabled: boolean = false;
  _supportedCurrencies: Currency[] = [];
  _interestRateTerms: DepositInterestRateTerms = DepositInterestRateTerms.TIERED_PER_BALANCE;
  depositInterestRateTermsEnum = DepositInterestRateTerms;
  _interestCalculationMethod: InterestCalculationMethod | null = null;

  public constructor(public languageService: LanguageService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  writeValue(value: TieredInterestItem[]): void {
    this._value = value;
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

  propagateChange = (_: TieredInterestItem[]) => { };
  propagateTouched = (_: TieredInterestItem[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: Currency[]) {
    this._supportedCurrencies = supportedCurrencies;
    this.populateCurrenciesToUi(this._supportedCurrencies);
  }

  get supportedCurrencies(): Currency[] {
    return this._supportedCurrencies;
  }

  get value(): TieredInterestItem[] {
    return this._value;
  }

  @Input()
  set interestRateTerms(interestRateTerms: DepositInterestRateTerms) {
    this._interestRateTerms = interestRateTerms;
  }

  get interestRateTerms(): DepositInterestRateTerms {
    return this._interestRateTerms;
  }

  @Input()
  set interestCalculationMethod(interestCalculationMethod: InterestCalculationMethod | null) {
    this._interestCalculationMethod = interestCalculationMethod;
  }

  get interestCalculationMethod(): InterestCalculationMethod | null {
    return this._interestCalculationMethod;
  }

  addTierClick($event: MouseEvent) {
    if (this.supportedCurrencies.length > 0) {
      var currency = this.supportedCurrencies[0];
      if (this._value.length > 0) {
        const last = this._value[this._value.length - 1];
        for (const c of this.supportedCurrencies) {
          if (c.id == last.currencyId) {
            currency = c;
            break;
          }
        }
      }
      var item = new TieredInterestItem();
      item.currencyId = currency.id;
      this._value.push(item);
      this.propagateChange(this._value);
    }
  }

  removeTierClick($event: MouseEvent, index: number) {
    this._value.splice(index, 1);
    this.propagateChange(this._value);
  }

  inerestTierCurrencyChanged($event: any, item: TieredInterestItem) {
    this.propagateChange(this._value);
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
        this.propagateChange(this._value);
      }
    } else {
      this._value.splice(0, this._value.length);
      this.propagateChange(this._value);
    }
  }

  private checkAndUpdateCurrency(currency: Currency): boolean {
    if (this._supportedCurrencies.length < 1) {
      this._value.splice(0, this._value.length);
      return true;
    } else {
      // Check tier
      if (this._value.length < 1) {
        var item = new TieredInterestItem();
        item.currencyId = currency.id;
        this._value.push(item);
        return true;
      }
    }
    return false;
  }
}
