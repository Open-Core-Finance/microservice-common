import { AfterViewInit, Component, EventEmitter, Inject, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { Holiday } from 'src/app/classes/organizations/Holiday';
import { environment } from 'src/environments/environment';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
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

@Component({
  selector: 'app-add-holiday',
  templateUrl: './add-holiday.component.html',
  styleUrl: './add-holiday.component.sass'
})
export class AddHolidayComponent extends GeneralEntityAddComponent<Holiday> implements OnInit, OnDestroy, AfterViewInit {

  @ViewChild('holidayDatePicker')
  holidayDatePicker!: MatDatepicker<any>;

  addHolidayForm = new FormGroup({
    index: new FormControl(0),
    id: new FormControl(""),
    description: new FormControl('', {nonNullable: true}),
    repeatYearly: new FormControl(false, {nonNullable: true}),
    holidayDate: new FormControl<any>(new Date(), {nonNullable: true})
  });

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, @Inject(MAT_DATE_FORMATS) public config: CustomDateFormat) {
      super(languageService, commonService, restService, http, formBuilder, organizationService);
  }

  ngAfterViewInit(): void {
  }
  
  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  protected override getAddForm(): FormGroup<any> {
    return this.addHolidayForm;
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
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.holiday;
  }

  protected override newEmptyEntity(): Holiday {
    return new Holiday();
  }
}
