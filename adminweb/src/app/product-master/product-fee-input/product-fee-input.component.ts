import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Currency } from 'src/app/classes/Currency';
import { MonthlyPayOption, ProductFee } from 'src/app/classes/products/Product';
import { ProductFeeType } from 'src/app/classes/products/ProductFeeType';
import { CurrencyService } from 'src/app/services/currency.service';
import { LanguageService } from 'src/app/services/language.service';

@Component({
  selector: 'app-product-fee-input',
  templateUrl: './product-fee-input.component.html',
  styleUrl: './product-fee-input.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => ProductFeeInputComponent),
      multi: true
    }
  ]
})
export class ProductFeeInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  selectedFees: ProductFee[] = [];
  supportedCurrenciesObject: Currency[] = [];
  productFeeTypeEnum = ProductFeeType;
  monthlyPayOptionEnum = MonthlyPayOption;
  currencies: Currency[] = [];
  currenciesSubscription: Subscription | undefined;
  lastSupportedCurrencies: string[] | undefined;

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

  writeValue(value: ProductFee[]): void {
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

  propagateChange = (_: ProductFee[]) => { };
  propagateTouched = (_: ProductFee[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: string[]) {
    this.supportedCurrenciesObject = [];
    this.lastSupportedCurrencies = supportedCurrencies;
    this.populateCurrenciesToUi(supportedCurrencies);
  }

  private populateCurrenciesToUi(supportedCurrencies: string[]) {
    this.currencies.forEach((value, index, arr) =>{
      for(let  i = 0; i < supportedCurrencies.length; i++) {
        const c = supportedCurrencies[i];
        if (c == value.id) {
          this.supportedCurrenciesObject.push(value);
          break;
        }
      }
    });
  }

  addFeeClick() {
    var item = new ProductFee();
    if (this.supportedCurrenciesObject.length > 0) {
      item.currencyId = this.supportedCurrenciesObject[0].id;
    }
    this.selectedFees.push(item);
  }

  removeFee($event: any, index: number) {
    this.selectedFees.splice(index, 1);
  }
}
