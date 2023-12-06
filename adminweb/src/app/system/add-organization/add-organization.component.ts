import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, Input, OnDestroy, Output, OnInit, LOCALE_ID, Inject } from '@angular/core';
import { DatePipe, formatDate } from '@angular/common';
import { FormControl, FormGroup } from '@angular/forms';
import { Subscription, Observable } from 'rxjs';
import { startWith, map } from 'rxjs/operators';
import { Currency } from 'src/app/classes/Currency';
import { DayOfWeek } from 'src/app/classes/DayOfWeek';
import { GeneralApiResponse } from 'src/app/classes/GeneralApiResponse';
import { Organization } from 'src/app/classes/Organization';
import { EntitiesService } from 'src/app/services/EntitiesService';
import { CommonService } from 'src/app/services/common.service';
import { LanguageService } from 'src/app/services/language.service';
import { RestService } from 'src/app/services/rest.service';
import { environment } from 'src/environments/environment';
import { Timezone, _filterTimezoneName, _filterTimezone, DATE_FORMAT_CHARS } from 'src/app/const/TimeZoneList';

@Component({
  selector: 'app-add-organization',
  templateUrl: './add-organization.component.html',
  styleUrls: ['./add-organization.component.sass']
})
export class AddOrganizationComponent implements OnDestroy, OnInit {

  @Output() cancel = new EventEmitter();
  @Output() save = new EventEmitter();
  _addingItem: Organization | null = null;

  currencies: Currency[] = [];
  currencySubscription: Subscription | undefined;
  timeZoneListObservable!: Observable<Timezone[]>;
  tineZoneSelectionSubscription!: Subscription;

  addOrganizationForm = new FormGroup({
    index: new FormControl(0),
    id: new FormControl(""),
    name: new FormControl(''),
    streetAddressLine1: new FormControl(""),
    city: new FormControl(""),
    state: new FormControl(""),
    country: new FormControl(""),
    zipPostalCode: new FormControl(""),
    phoneNumber: new FormControl(""),
    email: new FormControl(""),
    currency: new FormControl<Currency | null>(null),
    decimalMark: new FormControl(""),
    timezone: new FormControl(""),
    localDateFormat: new FormControl("", {nonNullable: true}),
    localDateTimeFormat: new FormControl(""),
    logoUrl: new FormControl(""),
    iconUrl: new FormControl(""),
    nonWorkingDays: new FormControl<DayOfWeek[]>([])
  });
  message: Record<string, any[]> = {
    success: [],
    error: []
  };

  constructor(public languageService: LanguageService, private commonService: CommonService, @Inject( LOCALE_ID ) private locale: string,
    private restService: RestService, private http: HttpClient, private entitiesService: EntitiesService) {
      this.currencySubscription = entitiesService.organizationObservableMap.get(entitiesService.ENTITY_TYPE_CURRENCY)?.subscribe(
         currencies => this.currencies = currencies
      );
  }

  ngOnInit(): void {
    this.timeZoneListObservable = this.addOrganizationForm.get('timezone')!.valueChanges.pipe(
      startWith(''),
      map(value => _filterTimezone(value || '')),
    );
    if (this.tineZoneSelectionSubscription) {
      this.tineZoneSelectionSubscription.unsubscribe();
    }
    this.tineZoneSelectionSubscription = this.timeZoneListObservable.subscribe( zones => {
      if (zones.length == 1) {
        const item = zones[0];
        this.addOrganizationForm.patchValue({country: item.Country});
      }
    });
  }

  ngOnDestroy(): void {
    if (this.currencySubscription) {
      this.currencySubscription.unsubscribe();
    }
    if (this.tineZoneSelectionSubscription) {
      this.tineZoneSelectionSubscription.unsubscribe();
    }
  }

  saveClick($event: any): any {
    this.clearMessages();
    const formData = this.addOrganizationForm.value;
    const errors = this.message['error'];
    if (this.commonService.isNullOrEmpty(formData.name)) {
      errors.push("name_empty")
    }
    if (formData.currency == null || this.commonService.isNullOrEmpty(formData.currency.id)) {
      errors.push("currency_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.timezone)) {
      errors.push("timezone_empty")
    }
    const dateFormat = formData.localDateFormat;
    if (!this.validDateTimeFormat(dateFormat)) {
      errors.push("invalid_date_format");
    }
    const datetimeFormat = formData.localDateTimeFormat;
    if (!this.validDateTimeFormat(datetimeFormat)) {
      errors.push("invalid_date_time_format");
    }
    if (this.commonService.isNullOrEmpty(formData.phoneNumber)) {
      errors.push("phone_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.email)) {
      errors.push("email_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.country)) {
      errors.push("country_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.id)) {
      delete formData.id;
    }
    if (errors.length < 1) {
      const requestHeaders = this.restService.initApplicationJsonRequestHeaders();
      const serviceUrl = environment.apiUrl.organization + "/create-or-update";
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

  validDateTimeFormat(dateTimeFormat: string | null | undefined) : boolean {
    if (dateTimeFormat == null || dateTimeFormat == undefined) {
      return false;
    }
    for ( let i = 0; i < dateTimeFormat.length; i++) {
       if (DATE_FORMAT_CHARS.indexOf(dateTimeFormat.charAt(i)) < 0) {
        return false;
       }
    }
    return true;
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

  @Input() set addingItem(item: Organization| null) {
    this._addingItem = item;
    if (item) {
      this.addOrganizationForm.setValue(item);
    } else {
      this.addOrganizationForm.setValue(new Organization());
    }
  }

  currencyChanged(currency: Currency) {
    this.addOrganizationForm.patchValue({
      decimalMark: currency.decimalMark
    });
  }
}
