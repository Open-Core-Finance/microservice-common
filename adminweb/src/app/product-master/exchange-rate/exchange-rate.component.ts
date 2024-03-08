import { Component } from '@angular/core';
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

  override get localizePrefix(): string {
    return "exchangeRate";
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("sellRate", labelKeyPrefix + "sellRate"));
    result.push(new TableColumnUi("buyRate", labelKeyPrefix + "buyRate"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.exchangeRate;
  }

  override createNewItem(): ExchangeRate {
    return new ExchangeRate();
  }
}
