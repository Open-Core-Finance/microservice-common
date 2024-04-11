import {Component, forwardRef, OnInit, OnDestroy, Input, Output, EventEmitter} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { MatFormFieldAppearance } from '@angular/material/form-field';
import { Observable } from 'rxjs';
import { UiSelectItem } from 'src/app/classes/ui/UiFormInput';
import { LanguageService } from 'src/app/services/language.service';

@Component({
  selector: 'app-reactive-form-input',
  templateUrl: './reactive-form-input.component.html',
  styleUrl: './reactive-form-input.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => ReactiveFormInputComponent),
      multi: true
    }
  ]
})
export class ReactiveFormInputComponent implements OnInit, ControlValueAccessor, OnDestroy {

  value: any;
  @Input()
  inputType: string = "text";
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
  postFixLabelKey: string = "";
  @Input()
  autoComleteItems: Observable<UiSelectItem[]> | undefined;
  @Input()
  readonly = false;
  @Input()
  formInputName = "";
  @Output()
  inputEventOut = new EventEmitter();

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

  onInput($event: any, value: any) {
    if (this.inputEventOut) {
      this.inputEventOut.emit({event: $event, value: value, name: this.formInputName});
    }
  }
}
