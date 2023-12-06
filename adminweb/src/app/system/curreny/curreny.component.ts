import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Currency } from 'src/app/classes/Currency';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-curreny',
  standalone: false,
  templateUrl: './curreny.component.html',
  styleUrl: './curreny.component.sass'
})
export class CurrenyComponent extends TableComponent<Currency> {

  override buildTableColumns(): string[] {
    return ["index", "id", "name", "symbol", "decimalMark", "symbolAtBeginning", "action"];
  }

  override ngAfterViewInit(): void {
    super.ngAfterViewInit();
    const order = new UiOrderEvent();
    order.active = "id";
    order.direction = "asc";
    this.changeOrder({ order });
  }

  getServiceUrl() {
    return environment.apiUrl.currency;
  }

  override getDeleteConfirmContent(item: Currency): string {
    return this.languageService.formatLanguage("currencyDeleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: Currency): string {
    return this.languageService.formatLanguage("currencyDeleteConfirmTitle", []);
  }

  override createNewItem(): Currency {
    return new Currency();
  }
}
