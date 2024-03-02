import { ThemePalette } from "@angular/material/core";
import { MatFormFieldAppearance } from "@angular/material/form-field";

export class UiFormItem {

    formControlName: string = "";
    additionClass: string = "";
    appearance: MatFormFieldAppearance = "fill";
    colorScheme: ThemePalette = "primary";
    required: boolean = false;

    constructor(formControlName: string) {
        this.formControlName = formControlName;
    }
}

export class UiFormInput extends UiFormItem {

  inputType: string = "text";

  addLabel: boolean = true;

  placeHolderKey: string = "";

  constructor(placeHolderKey: string, formControlName: string){
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
    lableKey = "";
    selectValue: any;
}

export class UiFormCustomContent extends UiFormItem  {

  contentSelect: string = "";

  constructor(contentSelect: string, formControlName: string){
    super(formControlName);
    this.contentSelect = contentSelect;
  }
}