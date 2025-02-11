import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { AppComponent } from 'src/app/app.component';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { environment } from 'src/environments/environment';
import { AddCorporateCustomerComponent } from '../add-corporate-customer/add-corporate-customer.component';
import { CorporateCustomer } from 'src/app/classes/customers/CorporateCustomer';

@Component({
    selector: 'app-corporate-customer',
    imports: [CommonModule, SharedModule, AddCorporateCustomerComponent],
    templateUrl: './corporate-customer.component.html',
    styleUrl: './corporate-customer.component.sass'
})
export class CorporateCustomerComponent extends TableComponent<CorporateCustomer> {

  override permissionResourceName(): string {
    return "corporateCustomer";
  }

  parent: AppComponent | undefined;

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("taxNumber", labelKeyPrefix + "taxNumber"));
    result.push(new TableColumnUi("startDate", labelKeyPrefix + "startDate", {dateFormat: "yyyy-MM-dd"}));
    result.push(new TableColumnUi("contactPhone", labelKeyPrefix + "contactPhone"));
    result.push(new TableColumnUi("contactEmail", labelKeyPrefix + "contactEmail"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.corporateCustomer;
  }

  override createNewItem(): CorporateCustomer {
    return new CorporateCustomer();
  }

  override get indexColumnLabelKey(): string {
    return "";
  }
}
