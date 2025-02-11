import { Component } from '@angular/core';
import {TableComponent} from "../../generic-component/TableComponent";
import {UiOrderEvent} from "../../classes/UiOrderEvent";
import {environment} from "../../../environments/environment";
import { CryptoProduct } from 'src/app/classes/products/CryptoProduct';
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
    selector: 'app-crypto-product',
    templateUrl: './crypto-product.component.html',
    styleUrl: './crypto-product.component.sass',
    standalone: false
})
export class CryptoProductComponent extends TableComponent<CryptoProduct> {

  override permissionResourceName(): string {
    return "cryptoproduct";
  }

  override get localizePrefix(): string {
    return "cryptoProduct";
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
    return environment.apiUrl.cryptoProduct;
  }

  override createNewItem(): CryptoProduct {
    return new CryptoProduct();
  }
}
