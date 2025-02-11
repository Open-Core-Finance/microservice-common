import { Component } from '@angular/core';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { Branch } from 'src/app/classes/organizations/Branch';
import { environment } from 'src/environments/environment';
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
    selector: 'app-branch',
    templateUrl: './branch.component.html',
    styleUrl: './branch.component.sass',
    standalone: false
})
export class BranchComponent extends TableComponent<Branch> {

  override permissionResourceName(): string {
    return "branch";
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("phoneNumber", "phoneNumber"));
    result.push(new TableColumnUi("email", "email"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.branch;
  }

  override createNewItem(): Branch {
    return new Branch();
  }
}
