import { Component } from '@angular/core';
import { environment } from '../../../environments/environment';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatDialogModule} from '@angular/material/dialog';
import { Role } from '../../classes/Role';
import { AddRoleComponent } from '../add-role/add-role.component';
import { PermissionComponent } from '../permission/permission.component';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
  selector: 'app-role',
  standalone: true,
  imports: [MatProgressSpinnerModule, MatTableModule, MatIconModule, CommonModule, SharedModule,
    MatPaginatorModule, MatFormFieldModule, MatDialogModule, AddRoleComponent, PermissionComponent],
  templateUrl: './role.component.html',
  styleUrl: './role.component.sass'
})
export class RoleComponent extends TableComponent<Role> {

  selectedRole: Role | null = null;

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.role;
  }

  override createNewItem(): Role {
    return new Role("", "", null, this.organizationService.organization);
  }

  permissionClick($event: any, element: Role) {
    this.selectedRole = element;
  }

  permissionCancelClicked($event: any) {
    this.selectedRole = null;
  }

  permissionSaveClicked($event: any) {
    this.selectedRole = null;
  }

  override permissionResourceName(): string {
    return "role";
  }
}