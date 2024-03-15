import { Component } from '@angular/core';
import { ExchangeRate } from 'src/app/classes/products/ExchangeRate';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';
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
    result.push(new TableColumnUi("fromCurrency", labelKeyPrefix + "fromCurrency"));
    result.push(new TableColumnUi("toCurrency", labelKeyPrefix + "toCurrency"));
    result.push(new TableColumnUi("sellRate", labelKeyPrefix + "sellRate", {numberFormat: '1.0-2'}));
    result.push(new TableColumnUi("buyRate", labelKeyPrefix + "buyRate", {numberFormat: '1.0-2'}));
    result.push(new TableColumnUi("margin", labelKeyPrefix + "margin", {numberFormat: '1.0-2'}));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.exchangeRate;
  }

  override createNewItem(): ExchangeRate {
    return new ExchangeRate();
  }

  override getDeleteConfirmContent(item: ExchangeRate): string {
    const messageKey = this.localizePrefix + ".deleteConfirmContent";
    return this.languageService.formatLanguage(messageKey, [item.fromCurrency, item.toCurrency]);
  }

  protected override getDeleteUrl(item: ExchangeRate): string {
    var id = {fromCurrency: item.fromCurrency, toCurrency: item.toCurrency};
    return this.getServiceUrl() + "/" + JSON.stringify(id);
  }
}
