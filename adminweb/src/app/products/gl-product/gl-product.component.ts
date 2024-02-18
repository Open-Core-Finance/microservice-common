import { Component } from '@angular/core';
import {TableComponent} from "../../generic-component/TableComponent";
import {UiOrderEvent} from "../../classes/UiOrderEvent";
import {environment} from "../../../environments/environment";
import { GlProduct } from 'src/app/classes/products/GlProduct';

@Component({
  selector: 'app-gl-product',
  templateUrl: './gl-product.component.html',
  styleUrl: './gl-product.component.sass'
})
export class GlProductComponent extends TableComponent<GlProduct> {
  
  override permissionResourceName(): string {
    return "glproduct";
  }

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
