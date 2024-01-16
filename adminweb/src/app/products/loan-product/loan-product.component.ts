import { Component } from '@angular/core';
import {TableComponent} from "../../generic-component/TableComponent";
import {UiOrderEvent} from "../../classes/UiOrderEvent";
import {environment} from "../../../environments/environment";
import { LoanProduct } from 'src/app/classes/products/LoanProduct';

@Component({
  selector: 'app-loan-product',
  templateUrl: './loan-product.component.html',
  styleUrl: './loan-product.component.sass'
})
export class LoanProductComponent extends TableComponent<LoanProduct> {

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
