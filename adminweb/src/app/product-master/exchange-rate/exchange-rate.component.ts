import { Component } from '@angular/core';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { ExchangeRate } from 'src/app/classes/products/ExchangeRate';
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-exchange-rate',
  templateUrl: './exchange-rate.component.html',
  styleUrl: './exchange-rate.component.sass'
})
export class ExchangeRateComponent extends TableComponent<ExchangeRate> {

  override permissionResourceName(): string {
    return "exchangerate";
  }

  override newEmptyTableUi(): TableUi {
    return new TableUi("exchangeRate.error.");
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = "exchangeRate.";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("sellRate", labelKeyPrefix + "sellRate"));
    result.push(new TableColumnUi("buyRate", labelKeyPrefix + "buyRate"));
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
    return environment.apiUrl.exchangeRate;
  }

  override getDeleteConfirmContent(item: ExchangeRate): string {
    return this.languageService.formatLanguage("exchangeRate.deleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: ExchangeRate): string {
    return this.languageService.formatLanguage("exchangeRate.deleteConfirmTitle", []);
  }

  override createNewItem(): ExchangeRate {
    return new ExchangeRate();
  }
}
