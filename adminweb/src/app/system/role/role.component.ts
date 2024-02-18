import { Component } from '@angular/core';
import { UiOrderEvent } from '../../classes/UiOrderEvent';
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

  override buildTableColumns(): string[] {
    return ["index", "id", "name", "action"];
  }

  override ngAfterViewInit(): void {
    super.ngAfterViewInit();
    const order = new UiOrderEvent();
    order.active = "id";
    order.direction = "asc";
    this.changeOrder({ order });
  }

  getServiceUrl() {
    return environment.apiUrl.role;
  }

  override getDeleteConfirmContent(item: Role): string {
    return this.languageService.formatLanguage("role.deleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: Role): string {
    return this.languageService.formatLanguage("role.deleteConfirmTitle", []);
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