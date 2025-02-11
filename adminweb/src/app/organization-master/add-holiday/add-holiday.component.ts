import { AfterViewInit, ChangeDetectorRef, Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Holiday } from 'src/app/classes/organizations/Holiday';
import { environment } from 'src/environments/environment';
import { FormBuilder, FormGroup } from '@angular/forms';
import { LanguageService } from 'src/app/services/language.service';
import { CommonService } from 'src/app/services/common.service';
import { RestService } from 'src/app/services/rest.service';
import { HttpClient } from '@angular/common/http';
import { MatDatepicker } from '@angular/material/datepicker';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { CustomDateFormat } from 'src/app/classes/DateFormat';
import { formatDate } from '@angular/common';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { OrganizationService } from 'src/app/services/organization.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { UiFormInput, UiFormItem } from 'src/app/classes/ui/UiFormInput';
import { UiFormDate } from 'src/app/classes/ui/UiFormInput';
import { UiFormCheckbox } from 'src/app/classes/ui/UiFormInput';

@Component({
    selector: 'app-add-holiday',
    templateUrl: './add-holiday.component.html',
    styleUrl: './add-holiday.component.sass',
    standalone: false
})
export class AddHolidayComponent extends GeneralEntityAddComponent<Holiday> implements OnInit, OnDestroy, AfterViewInit {

  @ViewChild('holidayDatePicker')
  holidayDatePicker!: MatDatepicker<any>;

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService, @Inject(MAT_DATE_FORMATS) public config: CustomDateFormat) {
      super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
  }

  protected override buildFormItems(): UiFormItem[] {
    const formItems: UiFormItem[] = [];
    const prefix = "holiday.";
    formItems.push(new UiFormInput(prefix + "description", "description"));
    formItems.push(new UiFormDate(prefix + "holidayDate", "holidayDate"));
    formItems.push(new UiFormCheckbox(prefix + "dateRange", "dateRange"));
    formItems.push(new UiFormCheckbox(prefix + "repeatYearly", "repeatYearly"));
    return formItems;
  }

  ngAfterViewInit(): void {
  }
  
  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  protected override validateFormData(formData: any): void {
    if (this.commonService.isNullOrEmpty(formData.description)) {
      this.message['error'].push("description_empty")
    }
    if (formData.holidayDate == null) {
      this.message['error'].push("holiday_date_empty")
    } else {
      formData.holidayDate = formatDate(formData.holidayDate, "yyyy-MM-dd", "en_US");
    }
    if (formData.repeatYearly == null) {
      formData.repeatYearly = false;
    }
    if (!formData.dateRange) {
      formData.toDate = formData.holidayDate;
    } else {
      formData.toDate = formatDate(formData.toDate, "yyyy-MM-dd", "en_US");
    }
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.holiday;
  }

  protected override newEmptyEntity(): Holiday {
    return new Holiday();
  }
}
