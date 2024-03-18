import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { DepositProductFee } from 'src/app/classes/products/DepositProduct';
import { MonthlyPayOption } from 'src/app/classes/products/Product';
import { DepositProductFeeType } from 'src/app/classes/products/ProductFeeType';
import { CurrencyService } from 'src/app/services/currency.service';
import { LanguageService } from 'src/app/services/language.service';

@Component({
  selector: 'app-deposit-product-fee-input',
  templateUrl: './deposit-product-fee-input.component.html',
  styleUrl: './deposit-product-fee-input.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DepositProductFeeInputComponent),
      multi: true
    }
  ]
})
export class DepositProductFeeInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  selectedFees: DepositProductFee[] = [];
  productFeeTypeEnum = DepositProductFeeType;
  monthlyPayOptionEnum = MonthlyPayOption;
  _supportedCurrencies: Currency[] = [];

  public constructor(public languageService: LanguageService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  writeValue(value: DepositProductFee[]): void {
    this.selectedFees = value;
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

  propagateChange = (_: DepositProductFee[]) => { };
  propagateTouched = (_: DepositProductFee[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: Currency[]) {
    this._supportedCurrencies = supportedCurrencies;
  }

  get supportedCurrencies(): Currency[] {
    return this._supportedCurrencies;
  }

  addFeeClick() {
    var item = new DepositProductFee();
    if (this._supportedCurrencies.length > 1) {
      item.currencyId = this._supportedCurrencies[1].id;
    } else if (this._supportedCurrencies.length > 0) {
      item.currencyId = this._supportedCurrencies[0].id;
    }
    this.selectedFees.push(item);
  }

  removeFee($event: any, index: number) {
    this.selectedFees.splice(index, 1);
  }
}
