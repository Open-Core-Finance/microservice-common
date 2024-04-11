import { ThemePalette } from "@angular/material/core";
import { MatFormFieldAppearance } from "@angular/material/form-field";
import { Observable } from "rxjs";

export class UiFormItem {

    formControlName: string = "";
    additionClass: string = "";
    appearance: MatFormFieldAppearance = "fill";
    colorScheme: ThemePalette = "primary";
    required: boolean = false;
    visibleFn: Function | null = null;
    disabledFn: Function | null = null;

    constructor(formControlName: string, visibleFn?: Function | null, disabledFn?: Function | null) {
        this.formControlName = formControlName;
        if (visibleFn) {
          this.visibleFn = visibleFn;
        }
        if (disabledFn) {
          this.disabledFn = disabledFn;
        }
    }
}

export class UiFormInput extends UiFormItem {

  inputType: string = "text";

  addLabel: boolean = true;

  placeHolderKey: string = "";
  postFixLabelKey = "";
  autoComleteItems: Observable<UiSelectItem[]> | undefined;

  constructor(placeHolderKey: string, formControlName: string, inputType?: string,
    visibleFn?: Function | null, disabledFn?: Function | null) {
    super(formControlName, visibleFn, disabledFn);
    this.placeHolderKey = placeHolderKey;
    if (inputType) {
      this.inputType = inputType;
    }
  }
}

export class UiFormSelect extends UiFormItem {
    labelKey: string = "";
    selectItems: UiSelectItem[];
    constructor(labelKey: string, selectItems: UiSelectItem[], formControlName: string,
      visibleFn?: Function | null, disabledFn?: Function | null) {
        super(formControlName, visibleFn, disabledFn);
        this.labelKey = labelKey;
        this.selectItems = selectItems;
    }
}

export class UiSelectItem {
    labelKey = "";
    selectValue: any;
}

export class UiFormCustomContent extends UiFormItem {

  contentSelect: string = "";

  constructor(contentSelect: string, formControlName: string,
    visibleFn?: Function | null, disabledFn?: Function | null) {
      super(formControlName, visibleFn, disabledFn);
    this.contentSelect = contentSelect;
  }
}

export class UiFormCheckbox extends UiFormItem {

  labelKey: string;

  constructor(labelKey: string, formControlName: string, visibleFn?: Function | null, disabledFn?: Function | null) {
    super(formControlName, visibleFn, disabledFn);
    this.labelKey = labelKey;
  }
}

export class UiFormDate extends UiFormItem {
  inputType: string = "text";

  addLabel: boolean = true;

  placeHolderKey: string = "";

  constructor(placeHolderKey: string, formControlName: string, visibleFn?: Function | null, disabledFn?: Function | null) {
    super(formControlName, visibleFn, disabledFn);
    this.placeHolderKey = placeHolderKey;
  }
}

export class UiFormTextarea extends UiFormItem {

  addLabel: boolean = true;

  placeHolderKey: string;

  constructor(placeHolderKey: string, formControlName: string, visibleFn?: Function | null, disabledFn?: Function | null) {
    super(formControlName, visibleFn, disabledFn);
    this.placeHolderKey = placeHolderKey;
  }
}

export class UiFormComplexInput extends UiFormItem {

  templateName: string;

  constructor(templateName: string, formControlName: string, visibleFn?: Function | null, disabledFn?: Function | null) {
    super(formControlName, visibleFn, disabledFn);
    this.templateName = templateName;
  }
}

export class ExpansionPanelInputGroup {
  hideToggle = false;
  expanded = true;
  headerTitle: string;
  headerDescription: string = "";
  formItems: UiFormItem[];
  visibleFn: Function | null = null;
  disabledFn?: Function | null = null;

  constructor(headerTitle: string, formItems: UiFormItem[], visibleFn?: Function | null, disabledFn?: Function | null) {
    this.headerTitle = headerTitle;
    this.formItems = formItems;
    if (visibleFn) {
      this.visibleFn = visibleFn;
    }
    if (disabledFn) {
      this.disabledFn = disabledFn;
    }
  }
}

export class UiFormDivider extends UiFormItem {

  inset: boolean = false;

  vertical = false;
  constructor(visibleFn?: Function | null, disabledFn?: Function | null) {
    super("", visibleFn, disabledFn);
  }
}

export class UiFormBigHeader extends UiFormItem {
  labelFn: Function;
  constructor(labelFn: Function, visibleFn?: Function | null, disabledFn?: Function | null) {
    super("", visibleFn, disabledFn);
    this.labelFn = labelFn;
  }
}
