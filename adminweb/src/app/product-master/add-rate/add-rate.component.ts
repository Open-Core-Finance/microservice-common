import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { GeneralApiResponse } from 'src/app/classes/GeneralApiResponse';
import { Rate, RateType } from 'src/app/classes/products/Rate';
import { RateSource } from 'src/app/classes/products/RateSource';
import { environment } from 'src/environments/environment';
import { formatDate } from '@angular/common';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';

@Component({
  selector: 'app-add-rate',
  templateUrl: './add-rate.component.html',
  styleUrl: './add-rate.component.sass'
})
export class AddRateComponent extends GeneralEntityAddComponent<Rate> implements OnInit {
  rateTypes = Object.keys(RateType);
  rateSources: RateSource[] = [];

  addRateForm = new FormGroup({
    index: new FormControl(0),
    id: new FormControl(""),
    rateSourceId: new FormControl('', {nonNullable: true}),
    type: new FormControl(RateType.INTEREST, {nonNullable: true}),
    note: new FormControl('', {nonNullable: false}),
    rateValue: new FormControl(0.0, {nonNullable: true}),
    validFrom: new FormControl<any>(new Date(), {nonNullable: true}),
    rateSourceName: new FormControl('', {nonNullable: false})
  });

  ngOnInit(): void {
    this.typeChange(this.addRateForm.value.rateValue)
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.rate;
  }
  protected override getAddForm(): FormGroup<any> {
    return this.addRateForm;
  }
  protected override validateFormData(formData: any): void {
    if (!formData.rateValue || formData.rateValue <= 0) {
      this.message['error'].push("value_error");
    }
  }

  protected override newEmptyEntity(): Rate {
    return new Rate();
  }

  protected override processingDataBeforeSubmit(formData: any): void {
    if (formData.validFrom) {
      formData.validFrom = formatDate(formData.validFrom, "yyyy-MM-dd'T'HH:mm:ssZ", "en_US");
    }
  }

  typeChange($event: any) {
    let headers = this.restService.initRequestHeaders();
    const serviceUrl = environment.apiUrl.rateSource + "/";
    const requestBody = "searchText=" + $event;
    this.rateSources = [];
    this.http.post<GeneralApiResponse>(serviceUrl, requestBody, { headers }).subscribe({
        next: (data: GeneralApiResponse) => {
          if (data.status === 0) {
            this.rateSources = (data.result as RateSource []);
          }
        }, error: (data: GeneralApiResponse) => {
          console.error(data);
        }
    });
  }
}
