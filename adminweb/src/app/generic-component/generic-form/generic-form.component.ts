import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { UserMessage } from 'src/app/classes/UserMessage';
import { UiFormCustomContent, UiFormInput, UiFormItem, UiFormSelect } from 'src/app/classes/ui/UiFormInput';
import { LanguageService } from 'src/app/services/language.service';

@Component({
  selector: 'app-generic-form',
  templateUrl: './generic-form.component.html',
  styleUrl: './generic-form.component.sass'
})
export class GenericFormComponent implements OnInit, OnDestroy {
  
  @Input()
  formTitleKey: string = "";
  @Input()
  formGroup: FormGroup = new FormGroup([]);
  @Input()
  formItems: UiFormItem[] = [];
  @Input()
  message: UserMessage = new UserMessage([], []);

  @Output() cancel = new EventEmitter();
  @Output() save = new EventEmitter();

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

  isFormCustomContent(item: UiFormItem) {
    return item instanceof UiFormCustomContent;
  }

  protected cancelClick($event: any): any {
    this.cancel.emit($event);
  }

  protected saveClick($event: any): any {
    this.save.emit($event);
  }
}
