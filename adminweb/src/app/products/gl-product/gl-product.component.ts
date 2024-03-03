import { Component } from '@angular/core';
import {TableComponent} from "../../generic-component/TableComponent";
import {UiOrderEvent} from "../../classes/UiOrderEvent";
import {environment} from "../../../environments/environment";
import { GlProduct } from 'src/app/classes/products/GlProduct';
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
  selector: 'app-gl-product',
  templateUrl: './gl-product.component.html',
  styleUrl: './gl-product.component.sass'
})
export class GlProductComponent extends TableComponent<GlProduct> {
  
  override permissionResourceName(): string {
    return "glproduct";
  }

  override newEmptyTableUi(): TableUi {
    return new TableUi("glProduct.error.");
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
    return environment.apiUrl.glProduct;
  }

  override getDeleteConfirmContent(item: GlProduct): string {
    return this.languageService.formatLanguage("glProduct.deleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: GlProduct): string {
    return this.languageService.formatLanguage("glProduct.deleteConfirmTitle", []);
  }

  override createNewItem(): GlProduct {
    return new GlProduct();
  }
}
