import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { UserMessage } from 'src/app/classes/UserMessage';
import { ExpansionPanelInputGroup, UiFormBigHeader, UiFormCheckbox, UiFormComplexInput, UiFormCustomContent, UiFormDate, UiFormDivider, UiFormInput, UiFormItem, UiFormSelect, UiFormTextarea } from 'src/app/classes/ui/UiFormInput';
import { LanguageService } from 'src/app/services/language.service';

@Component({
    selector: 'app-generic-form',
    templateUrl: './generic-form.component.html',
    styleUrl: './generic-form.component.sass',
    standalone: false
})
export class GenericFormComponent implements OnInit, OnDestroy {
  
  @Input()
  formTitleKey: string = "";
  @Input()
  readonlyTitleKey: string = "";
  @Input()
  formGroup: FormGroup = new FormGroup([]);
  @Input()
  formItems: UiFormItem[] = [];
  @Input()
  message: UserMessage = new UserMessage([], []);
  @Input()
  errorPrefix: string = "error.";

  @Input()
  complexInputTemplate: any;
  
  @Input()
  inputGroups: ExpansionPanelInputGroup[] = [];

  @Input()
  readonly = false;
  @Input()
  showCancel = true;
  @Input()
  showReset = false;

  @Output() cancel = new EventEmitter();
  @Output() save = new EventEmitter();
  @Output() onFieldInput = new EventEmitter();
  @Output() reset = new EventEmitter();
  @Output()
  optionSelected = new EventEmitter();

  constructor(public languageService: LanguageService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  isFormInput(item: UiFormItem) {
    return item instanceof UiFormInput;
  }

  isFormSelect(item: UiFormItem) {
    return item instanceof UiFormSelect;
  }

  isFormCheckbox(item: UiFormItem) {
    return item instanceof UiFormCheckbox;
  }

  isFormCustomContent(item: UiFormItem) {
    return item instanceof UiFormCustomContent;
  }

  isFormDate(item: UiFormItem) {
    return item instanceof UiFormDate;
  }

  isFormTextarea(item: UiFormItem) {
    return item instanceof UiFormTextarea;
  }

  isFormComplextInput(item: UiFormItem) {
    return item instanceof UiFormComplexInput;
  }

  isDevider(item: UiFormItem) {
    return item instanceof UiFormDivider;
  }

  isBigHeader(item: UiFormItem) {
    return item instanceof UiFormBigHeader;
  }

  protected cancelClick($event: any): any {
    this.cancel.emit($event);
  }

  protected resetClick($event: any): any {
    this.reset.emit($event);
  }

  protected saveClick($event: any): any {
    this.save.emit($event);
  }

  onOptionSelected($event: any) {
    if (this.optionSelected) {
      this.optionSelected.emit($event);
    }
  }
}
