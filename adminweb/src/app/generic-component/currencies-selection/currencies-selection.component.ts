import {Component, forwardRef, OnInit, ViewChild, ElementRef, OnDestroy} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import {LanguageService} from "../../services/language.service";
import { Currency } from 'src/app/classes/Currency';
import { EntitiesService } from 'src/app/services/EntitiesService';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-currencies-selection',
  templateUrl: './currencies-selection.component.html',
  styleUrl: './currencies-selection.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CurrenciesSelectionComponent),
      multi: true
    }
  ]
})
export class CurrenciesSelectionComponent implements OnInit, ControlValueAccessor, OnDestroy {

  selectedCurrenciesDisplay: Currency[] = [];
  currencies: Currency[] = [];
  _selectedCurrency: Currency | null = null;
  currenciesSubscription: Subscription | undefined;
  lastWriteValues: string[] | undefined;

  @ViewChild("selectionInput") input: ElementRef | undefined = undefined;

  constructor(public languageService: LanguageService, private entityService: EntitiesService) {
  }

  ngOnDestroy(): void {
    this.currenciesSubscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.currenciesSubscription?.unsubscribe();
    this.currenciesSubscription = this.entityService.organizationObservableMap.get(EntitiesService.ENTITY_TYPE_CURRENCY)?.subscribe( c => {
      this.currencies = c;
      this.populateValue(this.lastWriteValues);
    });
  }

  registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.propagateTouched = fn;
  }

  writeValue(value: string[]): void {
    this.lastWriteValues = value;
    this.populateValue(value);
  }

  private populateValue(value: string[] | undefined) {
    if (value !== undefined) {
      for (let i = 0; i < value.length; i++) {
        for (const b of this.currencies) {
          if (b.id == value[i]) {
            this.selectedCurrenciesDisplay.push(b);
          }
        }
      }
    }
  }

  propagateChange = (_: string[]) => { };
  propagateTouched = (_: string[]) => { };

  set selectedCurrency(selectedCurrency: Currency | null) {
    if (this.input) {
      this.input.nativeElement.value = "";
    }
    let contain = false;
    for (let branch of this.selectedCurrenciesDisplay) {
      if (branch.id == selectedCurrency?.id) contain = true;
    }
    if (!contain && selectedCurrency) {
      this.selectedCurrenciesDisplay.push(selectedCurrency);
      this.propagateChange(this.selectedCurrenciesDisplay.map((value, _, __) => value.id));
    }
    this._selectedCurrency = null;
  }

  get selectedCurrency(): Currency | null {
    return this._selectedCurrency;
  }

  isSelectedCurrency(currency: Currency) {
    for (const b of this.selectedCurrenciesDisplay) {
      if (currency.id == b.id) return true;
    }
    return false;
  }

  removeBranch(b: Currency) {
    let found = false;
    for (let i = 0; i < this.selectedCurrenciesDisplay.length; i++) {
      const branch = this.selectedCurrenciesDisplay[i];
      if (branch.id == b.id) {
        this.selectedCurrenciesDisplay.splice(i--, 1);
        found = true;
      }
    }
    if (found) {
      this.propagateChange(this.selectedCurrenciesDisplay.map((value, _, __) => value.id));
    }
    if (this.input) {
      this.input.nativeElement.value = "";
    }
    this._selectedCurrency = null;
  }
}
