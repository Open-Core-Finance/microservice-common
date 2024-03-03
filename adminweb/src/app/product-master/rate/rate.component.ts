import { Component } from '@angular/core';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { Rate } from 'src/app/classes/products/Rate';
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-rate',
  templateUrl: './rate.component.html',
  styleUrl: './rate.component.sass'
})
export class RateComponent extends TableComponent<Rate> {

  override permissionResourceName(): string {
    return "rate";
  }

  override newEmptyTableUi(): TableUi {
    return new TableUi("rate.error.");
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = "rate.";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("rateValue", labelKeyPrefix + "rateValue"));
    result.push(new TableColumnUi("validFrom", labelKeyPrefix + "validFrom"));
    result.push(new TableColumnUi("note", labelKeyPrefix + "note"));
    result.push(new TableColumnUi("rateSource", labelKeyPrefix + "rateSource"));
    return result;
  }

  override ngAfterViewInit(): void {
   super.ngAfterViewInit();
   const order = new UiOrderEvent();
   order.active = "id";
   order.direction = "asc";
   this.changeOrder({ order });
  }

  getServiceUrl() {
   return environment.apiUrl.rate;
  }

  override getDeleteConfirmContent(item: Rate): string {
   return this.languageService.formatLanguage("rate.deleteConfirmContent", [item.index]);
  }

  override getDeleteConfirmTitle(item: Rate): string {
   return this.languageService.formatLanguage("rate.deleteConfirmTitle", []);
  }

  override createNewItem(): Rate {
   return new Rate();
  }
}
