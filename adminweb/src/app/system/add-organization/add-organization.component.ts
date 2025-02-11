import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription, Observable } from 'rxjs';
import { startWith, map } from 'rxjs/operators';
import { Currency } from 'src/app/classes/Currency';
import { DayOfWeek } from 'src/app/classes/DayOfWeek';
import { Organization } from 'src/app/classes/Organization';
import { CurrencyService } from 'src/app/services/currency.service';
import { CommonService } from 'src/app/services/common.service';
import { LanguageService } from 'src/app/services/language.service';
import { RestService } from 'src/app/services/rest.service';
import { environment } from 'src/environments/environment';
import { Timezone, _filterTimezoneName, _filterTimezone, DATE_FORMAT_CHARS } from 'src/app/const/TimeZoneList';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { FormBuilder, FormControl } from '@angular/forms';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { OrganizationService } from 'src/app/services/organization.service';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
    selector: 'app-add-organization',
    templateUrl: './add-organization.component.html',
    styleUrls: ['./add-organization.component.sass'],
    standalone: false
})
export class AddOrganizationComponent extends GeneralEntityAddComponent<Organization> implements OnDestroy, OnInit {

  listDayOfWeeks = Object.keys(DayOfWeek);

  currencies: Currency[] = [];
  currencySubscription: Subscription | undefined;
  timeZoneListObservable!: Observable<Timezone[]>;
  tineZoneSelectionSubscription!: Subscription;

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService, private currencyService: CurrencyService) {
      super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
      this.currencySubscription?.unsubscribe();
      this.currencySubscription = currencyService.currenciesSubject.subscribe(
         currencies => this.currencies = currencies
      );
  }

  protected override get additionalFormGroupElement(): any {
    return { nonWorkingDays: new FormControl<DayOfWeek[]>([]) };
  }

  ngOnInit(): void {
    this.timeZoneListObservable = this.addForm.get('timezone')!.valueChanges.pipe(
      startWith(''),
      map(value => _filterTimezone(value || '')),
    );
    if (this.tineZoneSelectionSubscription) {
      this.tineZoneSelectionSubscription.unsubscribe();
    }
    this.tineZoneSelectionSubscription = this.timeZoneListObservable.subscribe( zones => {
      if (zones.length == 1) {
        const item = zones[0];
        this.addForm.patchValue({country: item.Country});
      }
    });
  }

  ngOnDestroy(): void {
    this.currencySubscription?.unsubscribe();
    this.tineZoneSelectionSubscription?.unsubscribe();
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.organization;
  }

  protected override validateFormData(formData: any): void {
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
  }

  protected override newEmptyEntity(): Organization {
    return new Organization();
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

  currencyChanged(currency: Currency) {
    this.addForm.patchValue({
      decimalMark: currency.decimalMark
    });
  }

  isDayOfWeekChecked(dayOfWeekName: string) {
    const dayOfWeeks = this.addForm.controls['nonWorkingDays'].value;
    if (dayOfWeeks) {
      for (var dayOfWeek of dayOfWeeks) {
        if (dayOfWeek === (dayOfWeekName as DayOfWeek)) {
          return true;
        }
      }
    }
    return false;
  }

  dayOfWeekChanged(dayOfWeekName: string, event: MatCheckboxChange) {
    const dayOfWeeks = this.addForm.controls['nonWorkingDays'].value;
    if (dayOfWeeks) {
      if (event.checked == false) {
        for (let i = 0; i < dayOfWeeks.length; i++) {
          if ((dayOfWeekName as DayOfWeek) == dayOfWeeks[i]) {
            dayOfWeeks.splice(i, 1);
            i--;
          }
        }
      } else {
        dayOfWeeks.push(dayOfWeekName as DayOfWeek);
      }
    }
  }
}
