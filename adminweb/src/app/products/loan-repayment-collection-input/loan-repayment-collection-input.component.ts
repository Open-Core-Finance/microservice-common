import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { PrePaymentRecalculation, RepaymentCollection, RepaymentType } from 'src/app/classes/products/Repayment';
import { Currency } from 'src/app/classes/Currency';
import { Subscription } from 'rxjs';
import { LanguageService } from 'src/app/services/language.service';
import { CurrencyService } from 'src/app/services/currency.service';
import {CdkDragDrop, CdkDropList, CdkDrag, moveItemInArray} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-loan-repayment-collection-input',
  templateUrl: './loan-repayment-collection-input.component.html',
  styleUrl: './loan-repayment-collection-input.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => LoanRepaymentCollectionInputComponent),
      multi: true
    }
  ]
})
export class LoanRepaymentCollectionInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  _supportedCurrencies: Currency[] = [];
  value: RepaymentCollection = new RepaymentCollection();

  prePaymentRecalculationEnum = PrePaymentRecalculation;
  allPrePaymentRecalculations = Object.keys(PrePaymentRecalculation);
  repaymentTypeEnum = RepaymentType;
  allRepaymentTypes = Object.keys(RepaymentType);

  public constructor(public languageService: LanguageService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  writeValue(value: RepaymentCollection): void {
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

  propagateChange = (_: RepaymentCollection[]) => { };
  propagateTouched = (_: RepaymentCollection[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: Currency[]) {
    this._supportedCurrencies = supportedCurrencies;
  }

  dropType(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.value.repaymentTypesOrder, event.previousIndex, event.currentIndex);
  }
}
