import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { IndividualCustomer } from 'src/app/classes/customers/IndividualCustomer';
import { AppComponent } from 'src/app/app.component';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { environment } from 'src/environments/environment';
import { AddIndividualCustomerComponent } from '../add-individual-customer/add-individual-customer.component';

@Component({
  selector: 'app-individual-customer',
  standalone: true,
  imports: [CommonModule, SharedModule, AddIndividualCustomerComponent],
  templateUrl: './individual-customer.component.html',
  styleUrl: './individual-customer.component.sass'
})
export class IndividualCustomerComponent extends TableComponent<IndividualCustomer> {

  override permissionResourceName(): string {
    return "individualCustomer";
  }

  parent: AppComponent | undefined;

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("firstName", labelKeyPrefix + "firstName"));
    result.push(new TableColumnUi("middleName", labelKeyPrefix + "middleName"));
    result.push(new TableColumnUi("lastName", labelKeyPrefix + "lastName"));
    result.push(new TableColumnUi("gender", labelKeyPrefix + "gender"));
    result.push(new TableColumnUi("contactEmail", labelKeyPrefix + "contactEmail"));
    result.push(new TableColumnUi("contactPhone", labelKeyPrefix + "contactPhone"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.individualCustomer;
  }

  override createNewItem(): IndividualCustomer {
    return new IndividualCustomer();
  }
}