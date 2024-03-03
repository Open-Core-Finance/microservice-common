import { Component } from '@angular/core';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { ProductCategory } from 'src/app/classes/products/ProductCategory';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { environment } from 'src/environments/environment';
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
  selector: 'app-product-category',
  templateUrl: './product-category.component.html',
  styleUrl: './product-category.component.sass'
})
export class ProductCategoryComponent extends TableComponent<ProductCategory> {

  override permissionResourceName(): string {
    return "productcategory";
  }

  override newEmptyTableUi(): TableUi {
    return new TableUi("productCategory.error.");
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = "productCategory.";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("type", labelKeyPrefix + "type"));
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
    return environment.apiUrl.productCategory;
  }

  override getDeleteConfirmContent(item: ProductCategory): string {
    return this.languageService.formatLanguage("productCategory.deleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: ProductCategory): string {
    return this.languageService.formatLanguage("productCategory.deleteConfirmTitle", []);
  }

  override createNewItem(): ProductCategory {
    return new ProductCategory();
  }
}
