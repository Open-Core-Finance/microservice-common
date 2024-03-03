import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { Branch } from 'src/app/classes/organizations/Branch';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { environment } from 'src/environments/environment';
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
  selector: 'app-branch',
  templateUrl: './branch.component.html',
  styleUrl: './branch.component.sass'
})
export class BranchComponent extends TableComponent<Branch> {

  override permissionResourceName(): string {
    return "branch";
  }

  override newEmptyTableUi(): TableUi {
    return new TableUi("branch.error.");
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = "branch.";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("phoneNumber", "phoneNumber"));
    result.push(new TableColumnUi("email", "email"));
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
    return environment.apiUrl.branch;
  }

  override getDeleteConfirmContent(item: Branch): string {
    return this.languageService.formatLanguage("branch.deleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: Branch): string {
    return this.languageService.formatLanguage("branch.deleteConfirmTitle", []);
  }

  override createNewItem(): Branch {
    return new Branch();
  }
}
