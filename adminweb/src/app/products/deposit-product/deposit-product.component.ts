import { Component } from '@angular/core';
import {TableComponent} from "../../generic-component/TableComponent";
import {UiOrderEvent} from "../../classes/UiOrderEvent";
import {environment} from "../../../environments/environment";
import {DepositProduct} from "../../classes/products/DepositProduct";
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
    selector: 'app-deposit-product',
    templateUrl: './deposit-product.component.html',
    styleUrl: './deposit-product.component.sass',
    standalone: false
})
export class DepositProductComponent extends TableComponent<DepositProduct> {

  override permissionResourceName(): string {
    return "depositproduct";
  }

  override get localizePrefix(): string {
    return "depositProduct";
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = "product.";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("category", labelKeyPrefix + "category"));
    result.push(new TableColumnUi("type", labelKeyPrefix + "type"));
    result.push(new TableColumnUi("lastModifiedDate", labelKeyPrefix + "lastModifiedDate", {dateFormat: "yyyy-MM-dd hh:mm:ss"}));
    result.push(new TableColumnUi("activated", labelKeyPrefix + "activated"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.depositProduct;
  }

  override createNewItem(): DepositProduct {
    return new DepositProduct();
  }
}
