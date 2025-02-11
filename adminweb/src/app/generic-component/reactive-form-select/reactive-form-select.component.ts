import {Component, forwardRef, OnInit, OnDestroy, Input, Output, EventEmitter} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { MatFormFieldAppearance } from '@angular/material/form-field';
import { UiSelectItem } from 'src/app/classes/ui/UiFormInput';
import { LanguageService } from 'src/app/services/language.service';

@Component({
    selector: 'app-reactive-form-select',
    templateUrl: './reactive-form-select.component.html',
    styleUrl: './reactive-form-select.component.sass',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => ReactiveFormSelectComponent),
            multi: true
        }
    ],
    standalone: false
})
export class ReactiveFormSelectComponent implements OnInit, ControlValueAccessor, OnDestroy {

  value: any;
  @Input()
  additionClass: string = "";
  @Input()
  appearance: MatFormFieldAppearance = "fill";
  @Input()
  colorScheme: ThemePalette = "primary";

  @Input()
  required: boolean = false;
  @Input()
  labelKey: string = "";

  @Input()
  selectItems: UiSelectItem[] = [];

  @Input()
  readonly = false;

  @Input()
  formInputName = "";

  @Output()
  optionSelected = new EventEmitter();

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

  onChanged($event: any) {
    this.propagateChange(this.value);
    this.onOptionSelected($event);
  }

  onOptionSelected($event: any) {
    if (this.optionSelected) {
      this.optionSelected.emit({event: $event, value: $event?.option?.value, name: this.formInputName});
    }
  }
}
