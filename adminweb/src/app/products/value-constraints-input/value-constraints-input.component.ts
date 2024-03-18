import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormBuilder, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { ValueConstraint } from 'src/app/classes/products/ValueConstraint';
import { LanguageService } from 'src/app/services/language.service';

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
  _supportedCurrencies: Currency[] = [];
  @Input()
  labelKey: string = "";
  @Input()
  labelKeyMinVal = "";
  @Input()
  labelKeyMaxVal = "";
  @Input()
  labelKeyDefaultVal = "";
  _value: ValueConstraint[] = [];
  @Input()
  sameConstraintForAllCurrency = true;

  public constructor(public languageService: LanguageService, protected formBuilder: FormBuilder) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  writeValue(value: ValueConstraint[]): void {
    this._value = value;
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
  set supportedCurrencies(supportedCurrencies: Currency[]) {
    this._supportedCurrencies = supportedCurrencies;
  }

  get supportedCurrencies(): Currency[] {
    return this._supportedCurrencies;
  }

  @Input()
  set value(value: ValueConstraint[]) {
    this.writeValue(value);
  }

  get value() {
    return this._value;
  }
}
