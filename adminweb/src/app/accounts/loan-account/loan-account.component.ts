import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { AppComponent } from 'src/app/app.component';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';
import { environment } from 'src/environments/environment';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { LoanAccount } from 'src/app/classes/accounts/LoanAccount';
import { AddLoanAccountComponent } from '../add-loan-account/add-loan-account.component';

@Component({
    selector: 'app-loan-account',
    imports: [CommonModule, SharedModule, AddLoanAccountComponent],
    templateUrl: './loan-account.component.html',
    styleUrl: './loan-account.component.sass'
})
export class LoanAccountComponent extends TableComponent<LoanAccount> {

  _customerId = 0;

  override permissionResourceName(): string {
    return "loanAccount";
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
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.loanAccount;
  }

  override createNewItem(): LoanAccount {
    var result = new LoanAccount();
    if (this._customerId > 0) {
      result.customerId = this._customerId;
    }
    return result;
  }

  @Input()
  set customerId(customerId: number) {
    this._customerId = customerId;
    if (this._customerId > 0) {
      this.searchText = '{"customerId": ' + this._customerId + '}';
    } else {
      this.searchText = "";
    }
    this.reloadData();
  }

  get customerId(): number {
    return this._customerId;
  }

}
