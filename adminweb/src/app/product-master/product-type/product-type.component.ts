import { Component } from '@angular/core';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { environment } from 'src/environments/environment';
import {ProductType} from "../../classes/products/ProductType";
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
    selector: 'app-product-type',
    templateUrl: './product-type.component.html',
    styleUrl: './product-type.component.sass',
    standalone: false
})
export class ProductTypeComponent extends TableComponent<ProductType> {

  override permissionResourceName(): string {
    return "producttype";
  }

  override get localizePrefix(): string {
    return "productType";
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("type", labelKeyPrefix + "type"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.productType;
  }

  override createNewItem(): ProductType {
    return new ProductType();
  }
}
