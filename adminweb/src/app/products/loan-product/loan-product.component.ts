import { Component } from '@angular/core';
import {TableComponent} from "../../generic-component/TableComponent";
import {UiOrderEvent} from "../../classes/UiOrderEvent";
import {environment} from "../../../environments/environment";
import { LoanProduct } from 'src/app/classes/products/LoanProduct';
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
  selector: 'app-loan-product',
  templateUrl: './loan-product.component.html',
  styleUrl: './loan-product.component.sass'
})
export class LoanProductComponent extends TableComponent<LoanProduct> {

  override permissionResourceName(): string {
    return "loanproduct";
  }

  override newEmptyTableUi(): TableUi {
    return new TableUi("loanProduct.error.");
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = "product.";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("category", labelKeyPrefix + "category"));
    result.push(new TableColumnUi("type", labelKeyPrefix + "type"));
    result.push(new TableColumnUi("lastModifyDate", labelKeyPrefix + "lastModifyDate"));
    result.push(new TableColumnUi("activated", labelKeyPrefix + "activated"));
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
    return environment.apiUrl.loanProduct;
  }

  override getDeleteConfirmContent(item: LoanProduct): string {
    return this.languageService.formatLanguage("loanProduct.deleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: LoanProduct): string {
    return this.languageService.formatLanguage("loanProduct.deleteConfirmTitle", []);
  }

  override createNewItem(): LoanProduct {
    return new LoanProduct();
  }
}
