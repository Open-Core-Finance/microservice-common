import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Currency } from 'src/app/classes/Currency';
import { Subscription } from 'rxjs';
import { FormControl, FormGroup } from '@angular/forms';
import { LanguageService } from 'src/app/services/language.service';
import { RestService } from 'src/app/services/rest.service';
import { HttpClient } from '@angular/common/http';
import { CommonService } from 'src/app/services/common.service';
import { EntitiesService } from 'src/app/services/EntitiesService';
import { GeneralApiResponse } from 'src/app/classes/GeneralApiResponse';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-add-curreny',
  templateUrl: './add-curreny.component.html',
  styleUrl: './add-curreny.component.sass'
})
export class AddCurrenyComponent {
  @Output() cancel = new EventEmitter();
  @Output() save = new EventEmitter();
  _addingItem: Currency | null = null;

  currencies: Currency[] = [];
  currencySubscription: Subscription | undefined;

  addCurrencyForm = new FormGroup({
    index: new FormControl(0),
    id: new FormControl(""),
    name: new FormControl('', {nonNullable: true}),
    symbol: new FormControl("", {nonNullable: true}),
    decimalMark: new FormControl("", {nonNullable: true}),
    symbolAtBeginning: new FormControl(false, {nonNullable: true})
  });
  message: Record<string, any[]> = {
    success: [],
    error: []
  };

  constructor(public languageService: LanguageService, private commonService: CommonService,
    private restService: RestService, private http: HttpClient, private entitiesService: EntitiesService) {
      this.currencySubscription = entitiesService.organizationObservableMap.get(entitiesService.ENTITY_TYPE_CURRENCY)?.subscribe(
         currencies => this.currencies = currencies
      );
  }

  saveClick($event: any): any {
    this.clearMessages();
    const formData = this.addCurrencyForm.value;
    if (this.commonService.isNullOrEmpty(formData.name)) {
      this.message['error'].push("name_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.symbol)) {
      this.message['error'].push("symbol_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.decimalMark)) {
      this.message['error'].push("decimal_mark_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.id)) {
      delete formData.id;
    }
    if (this.message['error'].length < 1) {
      const requestHeaders = this.restService.initApplicationJsonRequestHeaders();
      const serviceUrl = environment.apiUrl.currency + "/create-or-update";
      this.http.post<GeneralApiResponse>(serviceUrl, formData, {
        headers: requestHeaders, params: {}
      }).subscribe({
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
      });
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

  @Input() set addingItem(item: Currency| null) {
    this._addingItem = item;
    if (item) {
      this.addCurrencyForm.setValue(item);
    } else {
      this.addCurrencyForm.setValue(new Currency());
    }
  }
}
