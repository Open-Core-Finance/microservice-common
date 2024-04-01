import {Component, forwardRef, OnInit, OnDestroy, Input} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { MatFormFieldAppearance } from '@angular/material/form-field';
import { LanguageService } from 'src/app/services/language.service';

@Component({
  selector: 'app-reactive-form-textarea',
  templateUrl: './reactive-form-textarea.component.html',
  styleUrl: './reactive-form-textarea.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => ReactiveFormTextareaComponent),
      multi: true
    }
  ]
})
export class ReactiveFormTextareaComponent implements OnInit, ControlValueAccessor, OnDestroy {

  value: any;
  @Input()
  additionClass: string = "";
  @Input()
  appearance: MatFormFieldAppearance = "fill";
  @Input()
  colorScheme: ThemePalette = "primary";

  @Input()
  addLabel: boolean = true;
  @Input()
  required: boolean = false;
  @Input()
  placeHolderKey: string = "";

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

  writeValue(value: any): void {
    this.value = value;
  }

  propagateChange = (value: any) => {};
  propagateTouched = (_: any) => { };

  onChanged() {
    this.propagateChange(this.value);
  }
}
