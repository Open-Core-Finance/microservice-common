import { Component } from '@angular/core';
import {TableComponent} from "../../generic-component/TableComponent";
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

  override get localizePrefix(): string {
    return "glProduct";
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
    return environment.apiUrl.glProduct;
  }

  override createNewItem(): GlProduct {
    return new GlProduct();
  }
}
