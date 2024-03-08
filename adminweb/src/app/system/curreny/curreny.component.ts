import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Currency } from 'src/app/classes/Currency';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { environment } from 'src/environments/environment';
import { TableUi, TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
  selector: 'app-curreny',
  standalone: false,
  templateUrl: './curreny.component.html',
  styleUrl: './curreny.component.sass'
})
export class CurrenyComponent extends TableComponent<Currency> {

  override get localizePrefix(): string {
    return "currencyScreens";
  }
  
  override get tableUiColumns(): TableColumnUi[] {
    var languageService = this.languageService;
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", "currencyId"));
    result.push(new TableColumnUi("name", "currency"));
    result.push(new TableColumnUi("symbol", "currencySymbol"));
    result.push(new TableColumnUi("decimalMark", "decimalMark", {
      function(decimalMark: string): string {
        if (decimalMark == ".") {
          return languageService.formatLanguage("decimalMarkPeriod", []);
        } else if (decimalMark == ",") {
          return languageService.formatLanguage("decimalMarkComma", [])
        } else {
          return decimalMark;
        }
      }
    }));
    result.push(new TableColumnUi("symbolAtBeginning", "currencySymbolPosision", {labelPrefix: "currencySymbolPosision_"}));
    return result;
  }

  override permissionResourceName(): string {
    return "currency";
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
