import { ThemePalette } from "@angular/material/core";
import { MatFormFieldAppearance } from "@angular/material/form-field";

export class UiFormItem {

    formControlName: string = "";
    additionClass: string = "";
    appearance: MatFormFieldAppearance = "fill";
    colorScheme: ThemePalette = "primary";
    required: boolean = false;
    visibleFn: Function | null = null;

    constructor(formControlName: string) {
        this.formControlName = formControlName;
    }
}

export class UiFormInput extends UiFormItem {

  inputType: string = "text";

  addLabel: boolean = true;

  placeHolderKey: string = "";
  postFixLabelKey = "";

  constructor(placeHolderKey: string, formControlName: string) {
    super(formControlName);
    this.placeHolderKey = placeHolderKey;
  }
}

export class UiFormSelect extends UiFormItem {
    labelKey: string = "";
    selectItems: UiSelectItem[] = [];
    constructor(labelKey: string, selectItems: UiSelectItem[], formControlName: string){
        super(formControlName);
        this.labelKey = labelKey;
        this.selectItems = selectItems;
      }
}

export class UiSelectItem {
    labelKey = "";
    selectValue: any;
}

export class UiFormCustomContent extends UiFormItem  {

  contentSelect: string = "";

  constructor(contentSelect: string, formControlName: string){
    super(formControlName);
    this.contentSelect = contentSelect;
  }
}

export class UiFormCheckbox extends UiFormItem {

  labelKey: string = "";

  constructor(labelKey: string, formControlName: string){
    super(formControlName);
    this.labelKey = labelKey;
  }
}

export class UiFormDate extends UiFormItem {
  inputType: string = "text";

  addLabel: boolean = true;

  placeHolderKey: string = "";

  constructor(placeHolderKey: string, formControlName: string) {
    super(formControlName);
    this.placeHolderKey = placeHolderKey;
  }
}

export class UiFormTextarea extends UiFormItem {

  addLabel: boolean = true;

  placeHolderKey: string;

  constructor(placeHolderKey: string, formControlName: string) {
    super(formControlName);
    this.placeHolderKey = placeHolderKey;
  }
}

export class UiFormComplexInput extends UiFormItem {

  templateName: string;

  constructor(templateName: string, formControlName: string) {
    super(formControlName);
    this.templateName = templateName;
  }
}

export class ExpansionPanelInputGroup {
  hideToggle = false;
  expanded = true;
  formGroupName: string | null = null;
  headerTitle: string;
  headerDescription: string = "";
  formItems: UiFormItem[];
  visibleFn: Function | null = null;

  constructor(headerTitle: string, formItems: UiFormItem[]) {
    this.headerTitle = headerTitle;
    this.formItems = formItems;
  }
}

export class UiFormDivider extends UiFormItem {

  inset: boolean = false;

  vertical = false;
}