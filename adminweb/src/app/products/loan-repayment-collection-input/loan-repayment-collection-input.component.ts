import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { PrePaymentRecalculation, RepaymentCollection, RepaymentType } from 'src/app/classes/products/Repayment';
import { Currency } from 'src/app/classes/Currency';
import { Subscription } from 'rxjs';
import { LanguageService } from 'src/app/services/language.service';
import { EntitiesService } from 'src/app/services/EntitiesService';
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
  lastSupportedCurrencies: string[] | undefined;
  currencies: Currency[] = [];
  currenciesToDisplay: Currency[] = [];
  currenciesSubscription: Subscription | undefined;
  value: RepaymentCollection = new RepaymentCollection();

  prePaymentRecalculationEnum = PrePaymentRecalculation;
  allPrePaymentRecalculations = Object.keys(PrePaymentRecalculation);
  repaymentTypeEnum = RepaymentType;
  allRepaymentTypes = Object.keys(RepaymentType);

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

  writeValue(value: RepaymentCollection): void {
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

  propagateChange = (_: RepaymentCollection[]) => { };
  propagateTouched = (_: RepaymentCollection[]) => { };

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
            break;
          }
        }
      });
    }
  }

  dropType(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.value.repaymentTypesOrder, event.previousIndex, event.currentIndex);
  }
}
