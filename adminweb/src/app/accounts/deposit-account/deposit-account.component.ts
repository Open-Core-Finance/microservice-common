import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { AppComponent } from 'src/app/app.component';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';
import { environment } from 'src/environments/environment';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { AddDepositAccountComponent } from '../add-deposit-account/add-deposit-account.component';
import { DepositAccount } from 'src/app/classes/accounts/DepositAccount';

@Component({
  selector: 'app-deposit-account',
  standalone: true,
  imports: [CommonModule, SharedModule, AddDepositAccountComponent],
  templateUrl: './deposit-account.component.html',
  styleUrl: './deposit-account.component.sass'
})
export class DepositAccountComponent extends TableComponent<DepositAccount> {

  override permissionResourceName(): string {
    return "depositAccount";
  }

  parent: AppComponent | undefined;

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.permissionResourceName() + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("categoryName", labelKeyPrefix + "category"));
    result.push(new TableColumnUi("typeName", labelKeyPrefix + "type"));
    result.push(new TableColumnUi("description", labelKeyPrefix + "description"));
    result.push(new TableColumnUi("supportedCurrencies", labelKeyPrefix + "currencies"));
    result.push(new TableColumnUi("lastModifiedDate", labelKeyPrefix + "lastModifiedDate"));
    result.push(new TableColumnUi("lastModifiedBy", labelKeyPrefix + "lastModifiedBy", {subField: 'displayName'}));
    // result.push(new TableColumnUi("interestRate", labelKeyPrefix + "interestRateValue", {
    //   subField: "interestRateValues", jsonout: true
    // }));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.depositAccount;
  }

  override createNewItem(): DepositAccount {
    return new DepositAccount();
  }
}