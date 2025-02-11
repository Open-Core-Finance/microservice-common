import { Component } from '@angular/core';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { ProductCategory } from 'src/app/classes/products/ProductCategory';
import { environment } from 'src/environments/environment';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
    selector: 'app-product-category',
    templateUrl: './product-category.component.html',
    styleUrl: './product-category.component.sass',
    standalone: false
})
export class ProductCategoryComponent extends TableComponent<ProductCategory> {

  override permissionResourceName(): string {
    return "productcategory";
  }

  override get localizePrefix(): string {
    return "productCategory";
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
    return environment.apiUrl.productCategory;
  }

  override createNewItem(): ProductCategory {
    return new ProductCategory();
  }
}
