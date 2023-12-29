import { HttpClient } from '@angular/common/http';
import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { GeneralApiResponse } from 'src/app/classes/GeneralApiResponse';
import { Rate, RateType } from 'src/app/classes/products/Rate';
import { RateSource } from 'src/app/classes/products/RateSource';
import { CommonService } from 'src/app/services/common.service';
import { LanguageService } from 'src/app/services/language.service';
import { RestService } from 'src/app/services/rest.service';
import { environment } from 'src/environments/environment';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-add-rate',
  templateUrl: './add-rate.component.html',
  styleUrl: './add-rate.component.sass'
})
export class AddRateComponent implements OnInit {

  @Output() cancel = new EventEmitter();
  @Output() save = new EventEmitter();
  _addingItem: Rate | null = null;
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

  message: Record<string, any[]> = {
    success: [],
    error: []
  };

  constructor(public languageService: LanguageService, private commonService: CommonService,
    private restService: RestService, private http: HttpClient) {
  }

  ngOnInit(): void {
    this.typeChange(this.addRateForm.value.rateValue)
  }

  saveClick($event: any): any {
    this.clearMessages();
    const formData = this.addRateForm.value;
    if (!formData.rateValue || formData.rateValue <= 0) {
      this.message['error'].push("value_error");
    }
    if (this.commonService.isNullOrEmpty(formData.id)) {
      delete formData.id;
    }
    if (formData.validFrom) {
      formData.validFrom = formatDate(formData.validFrom, "yyyy-MM-dd'T'HH:mm:ssZ", "en_US");
    }
    console.log(formData);
    if (this.message['error'].length < 1) {
      const requestHeaders = this.restService.initApplicationJsonRequestHeaders();
      let serviceUrl = environment.apiUrl.rate + "/";
      var responseHandler = {
        next: (data: GeneralApiResponse) => {
          if (this.save) {
            $event.organization = data.result;
            this.save.emit($event);
          }
        }, error: (data: any) => {
          const message = this.message;
          if (data.statusText) {
            message['error'].push(data.statusText);
          } else if (data.statusCode) {
            message['error'].push(data.statusCode);
          } else {
            message['error'].push("Unknown error: " + JSON.stringify(data));
          }
        }
      };
      if (formData.id) {
        serviceUrl = environment.apiUrl.rate + "/" + formData.id;
        this.http.put<GeneralApiResponse>(serviceUrl, formData, {
          headers: requestHeaders, params: {}
        }).subscribe(responseHandler);
      } else {
        serviceUrl = environment.apiUrl.rate + "/create";
        this.http.post<GeneralApiResponse>(serviceUrl, formData, {
          headers: requestHeaders, params: {}
        }).subscribe(responseHandler);
      }
      
    }
  }

  cancelClick($event: any): any {
    if (this.cancel) {
      this.cancel.emit($event);
    }
  }

  clearMessages() {
    this.message = {
      success: [],
      error: []
    };
  }

  @Input() set addingItem(item: Rate| null) {
    this._addingItem = item;
    if (item) {
      this.addRateForm.setValue(item);
    } else {
      this.addRateForm.setValue(new Rate());
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
