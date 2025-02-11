import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { Region } from 'src/app/classes/geocode/Region';
import { environment } from 'src/environments/environment';
import { FormGroup } from '@angular/forms';
import { UiFormDivider, UiFormInput, UiFormItem } from 'src/app/classes/ui/UiFormInput';
import { UiFormTextarea } from 'src/app/classes/ui/UiFormInput';
import { UiFormCheckbox } from 'src/app/classes/ui/UiFormInput';
import { SharedModule } from 'src/app/generic-component/SharedModule';

@Component({
    selector: 'app-add-region',
    imports: [CommonModule, SharedModule],
    templateUrl: './add-region.component.html',
    styleUrl: './add-region.component.sass'
})
export class AddRegionComponent extends GeneralEntityAddComponent<Region> implements OnDestroy, OnInit {

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.region;
  }

  protected override validateFormData(formData: any): void {
    const errors = this.message['error'];
    if (this.commonService.isNullOrEmpty(formData.name)) {
      errors.push("name_empty")
    }
    if (!formData.translations || formData.translations.trim() == '' || formData.translations == '{}') {
      formData.translations = null;
    }
  }

  protected override newEmptyEntity(): Region {
    return new Region();
  }

  protected override buildFormItems(): UiFormItem[] {
    const formItems: UiFormItem[] = [];
    const prefix = "region.";
    formItems.push(new UiFormInput(prefix + "name", "name"));
    formItems.push(new UiFormCheckbox(prefix + "enabled", "enabled"));
    formItems.push(new UiFormInput(prefix + "wikiDataId", "wikiDataId"));
    formItems.push(new UiFormDivider());
    let item: UiFormItem = new UiFormTextarea(prefix + "translations", "translations");
    item.additionClass = "full-width";
    formItems.push(item);
    return formItems;
  }
}