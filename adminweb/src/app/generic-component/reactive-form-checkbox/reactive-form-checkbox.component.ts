import {Component, forwardRef, OnInit, OnDestroy, Input} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { MatFormFieldAppearance } from '@angular/material/form-field';
import { LanguageService } from 'src/app/services/language.service';

@Component({
  selector: 'app-reactive-form-checkbox',
  templateUrl: './reactive-form-checkbox.component.html',
  styleUrl: './reactive-form-checkbox.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => ReactiveFormCheckboxComponent),
      multi: true
    }
  ]
})
export class ReactiveFormCheckboxComponent implements OnInit, ControlValueAccessor, OnDestroy {

  value = false;
  @Input()
  additionClass: string = "";
  @Input()
  colorScheme: ThemePalette = "primary";

  @Input()
  required: boolean = false;
  @Input()
  labelKey: string = "";

  @Input()
  readonly = false;

  constructor(public languageService: LanguageService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.propagateTouched = fn;
  }

  writeValue(value: boolean): void {
    this.value = value;
  }

  propagateChange = (_: boolean) => {};
  propagateTouched = (_: boolean) => { };

  onChanged() {
    this.propagateChange(this.value);
  }
}
