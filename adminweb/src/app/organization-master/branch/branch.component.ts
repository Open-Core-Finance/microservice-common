import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { Branch } from 'src/app/classes/organizations/Branch';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-branch',
  templateUrl: './branch.component.html',
  styleUrl: './branch.component.sass'
})
export class BranchComponent extends TableComponent<Branch> {

  override permissionResourceName(): string {
    return "branch";
  }

  override buildTableColumns(): string[] {
    return ["index", "id", "name", "phoneNumber", "email", "action"];
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
