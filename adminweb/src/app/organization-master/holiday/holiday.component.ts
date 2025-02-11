import { Component } from '@angular/core';
import { Holiday } from 'src/app/classes/organizations/Holiday';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { environment } from 'src/environments/environment';
import { formatDate } from '@angular/common';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
    selector: 'app-holiday',
    templateUrl: './holiday.component.html',
    styleUrl: './holiday.component.sass',
    standalone: false
})
export class HolidayComponent extends TableComponent<Holiday> {

  override permissionResourceName(): string {
    return "holiday";
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("description", labelKeyPrefix + "description"));
    result.push(new TableColumnUi("holidayDate", labelKeyPrefix + "holidayDate", { complex: true }));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.holiday;
  }

  override getDeleteConfirmContent(item: Holiday): string {
    return this.languageService.formatLanguage(this.localizePrefix + ".deleteConfirmContent", [
      this.getHolidayDateLabel(item.holidayDate, item.repeatYearly)
    ]);
  }

  override createNewItem(): Holiday {
    return new Holiday();
  }

  getHolidayDateLabel(holidayDate: Date, repeatYearly: boolean): string {
    const locale = 'en-US';
    let dateFormat;
    if (repeatYearly) {
      dateFormat = this.languageService.formatLanguage(this.localizePrefix + ".holidayDateYearlyFormat", []);
    } else {
      dateFormat = this.languageService.formatLanguage(this.localizePrefix + ".holidayDateFormat", []);
    }
    return formatDate(holidayDate, dateFormat, locale);
  }
}
