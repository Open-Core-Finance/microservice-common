import { Component } from '@angular/core';
import { Holiday } from 'src/app/classes/organizations/Holiday';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { environment } from 'src/environments/environment';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-holiday',
  templateUrl: './holiday.component.html',
  styleUrl: './holiday.component.sass'
})
export class HolidayComponent extends TableComponent<Holiday> {

  override permissionResourceName(): string {
    return "holiday";
  }

  override buildTableColumns(): string[] {
    return ["index", "id", "description", "holidayDate", "repeatYearly", "action"];
  }

  override ngAfterViewInit(): void {
    super.ngAfterViewInit();
    const order = new UiOrderEvent();
    order.active = "id";
    order.direction = "asc";
    this.changeOrder({ order });
  }

  getServiceUrl() {
    return environment.apiUrl.holiday;
  }

  override getDeleteConfirmContent(item: Holiday): string {
    return this.languageService.formatLanguage("holiday.deleteConfirmContent", [
      this.getHolidayDateLabel(item.holidayDate, item.repeatYearly)
    ]);
  }

  override getDeleteConfirmTitle(item: Holiday): string {
    return this.languageService.formatLanguage("holiday.deleteConfirmTitle", []);
  }

  override createNewItem(): Holiday {
    return new Holiday();
  }

  getHolidayDateLabel(holidayDate: Date, repeatYearly: boolean): string {
    const locale = 'en-US';
    let dateFormat;
    if (repeatYearly) {
      dateFormat = this.languageService.formatLanguage("holiday.holidayDateYearlyFormat", []);
    } else {
      dateFormat = this.languageService.formatLanguage("holiday.holidayDateFormat", []);
    }
    return formatDate(holidayDate, dateFormat, locale);
  }
}
