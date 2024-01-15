import { Component } from '@angular/core';
import {TableComponent} from "../../generic-component/TableComponent";
import {UiOrderEvent} from "../../classes/UiOrderEvent";
import {environment} from "../../../environments/environment";
import { CryptoProduct } from 'src/app/classes/products/CryptoProduct';

@Component({
  selector: 'app-crypto-product',
  templateUrl: './crypto-product.component.html',
  styleUrl: './crypto-product.component.sass'
})
export class CryptoProductComponent extends TableComponent<CryptoProduct> {

  override buildTableColumns(): string[] {
    return ["index", "id", "name", "category", "type", "lastModifyDate", "activated", "action"];
  }

  override ngAfterViewInit(): void {
    super.ngAfterViewInit();
    const order = new UiOrderEvent();
    order.active = "id";
    order.direction = "asc";
    this.changeOrder({ order });
  }

  getServiceUrl() {
    return environment.apiUrl.cryptoProduct;
  }

  override getDeleteConfirmContent(item: CryptoProduct): string {
    return this.languageService.formatLanguage("cryptoProduct.deleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: CryptoProduct): string {
    return this.languageService.formatLanguage("cryptoProduct.deleteConfirmTitle", []);
  }

  override createNewItem(): CryptoProduct {
    return new CryptoProduct();
  }
}
