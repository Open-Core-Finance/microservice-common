import { Component } from '@angular/core';
import {TableComponent} from "../../generic-component/TableComponent";
import {UiOrderEvent} from "../../classes/UiOrderEvent";
import {environment} from "../../../environments/environment";
import {DepositProduct} from "../../classes/products/DepositProduct";
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
  selector: 'app-deposit-product',
  templateUrl: './deposit-product.component.html',
  styleUrl: './deposit-product.component.sass'
})
export class DepositProductComponent extends TableComponent<DepositProduct> {

  override permissionResourceName(): string {
    return "depositproduct";
  }

  override newEmptyTableUi(): TableUi {
    return new TableUi("depositProduct.error.");
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
    return environment.apiUrl.depositProduct;
  }

  override getDeleteConfirmContent(item: DepositProduct): string {
    return this.languageService.formatLanguage("depositProduct.deleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: DepositProduct): string {
    return this.languageService.formatLanguage("depositProduct.deleteConfirmTitle", []);
  }

  override createNewItem(): DepositProduct {
    return new DepositProduct();
  }
}
