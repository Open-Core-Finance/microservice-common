import { AfterViewInit, Component, EventEmitter, Inject, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { Holiday } from 'src/app/classes/organizations/Holiday';
import { environment } from 'src/environments/environment';
import { GeneralApiResponse } from 'src/app/classes/GeneralApiResponse';
import { FormControl, FormGroup } from '@angular/forms';
import { LanguageService } from 'src/app/services/language.service';
import { CommonService } from 'src/app/services/common.service';
import { RestService } from 'src/app/services/rest.service';
import { HttpClient } from '@angular/common/http';
import { MatDatepicker } from '@angular/material/datepicker';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { CustomDateFormat } from 'src/app/classes/DateFormat';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-add-holiday',
  templateUrl: './add-holiday.component.html',
  styleUrl: './add-holiday.component.sass'
})
export class AddHolidayComponent implements OnInit, OnDestroy, AfterViewInit {
  @Output() cancel = new EventEmitter();
  @Output() save = new EventEmitter();
  _addingItem: Holiday | null = null;

  @ViewChild('holidayDatePicker')
  holidayDatePicker!: MatDatepicker<any>;

  addHolidayForm = new FormGroup({
    index: new FormControl(0),
    id: new FormControl(""),
    description: new FormControl('', {nonNullable: true}),
    repeatYearly: new FormControl(false, {nonNullable: true}),
    holidayDate: new FormControl<any>(new Date(), {nonNullable: true})
  });
  message: Record<string, any[]> = {
    success: [],
    error: []
  };

  constructor(public languageService: LanguageService, private commonService: CommonService,
    private restService: RestService, private http: HttpClient, @Inject(MAT_DATE_FORMATS) public config: CustomDateFormat) {
  }

  ngAfterViewInit(): void {
  }
  
  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  saveClick($event: any): any {
    this.clearMessages();
    const formData = this.addHolidayForm.value;
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
    if (this.commonService.isNullOrEmpty(formData.id)) {
      delete formData.id;
    }
    if (this.message['error'].length < 1) {
      const requestHeaders = this.restService.initApplicationJsonRequestHeaders();
      let serviceUrl = environment.apiUrl.holiday + "/";
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
        serviceUrl = environment.apiUrl.holiday + "/" + formData.id;
        this.http.put<GeneralApiResponse>(serviceUrl, formData, {
          headers: requestHeaders, params: {}
        }).subscribe(responseHandler);
      } else {
        serviceUrl = environment.apiUrl.holiday + "/create";
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

  @Input() set addingItem(item: Holiday| null) {
    this._addingItem = item;
    if (item) {
      this.addHolidayForm.setValue(item);
    } else {
      this.addHolidayForm.setValue(new Holiday());
    }
  }
}
