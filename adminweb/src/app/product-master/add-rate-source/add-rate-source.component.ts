import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { RateType } from 'src/app/classes/products/Rate';
import { RateSource } from 'src/app/classes/products/RateSource';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-add-rate-source',
  templateUrl: './add-rate-source.component.html',
  styleUrl: './add-rate-source.component.sass'
})
export class AddRateSourceComponent extends GeneralEntityAddComponent<RateSource> {

  rateTypes = Object.keys(RateType);

  addRateSourceForm = new FormGroup({
    index: new FormControl(0),
    id: new FormControl(""),
    name: new FormControl('', {nonNullable: true}),
    type: new FormControl(RateType.INTEREST, {nonNullable: true}),
    note: new FormControl('', {nonNullable: false})
  });

  protected override getServiceUrl(): string {
    return environment.apiUrl.rateSource;
  }
  protected override getAddForm(): FormGroup<any> {
    return this.addRateSourceForm;
  }

  protected override validateFormData(formData: any): void {
    if (this.commonService.isNullOrEmpty(formData.name)) {
      this.message['error'].push("name_empty");
    }
    if (formData.type == null) {
      this.message['error'].push("type_empty");
    }
  }

  protected override newEmptyEntity(): RateSource {
    return new RateSource();
  }
}