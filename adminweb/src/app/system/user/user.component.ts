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
import { User } from '../../classes/User';
import { AddUserComponent } from '../add-user/add-user.component';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { TableUi, TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [MatProgressSpinnerModule, SharedModule, MatTableModule, MatIconModule, CommonModule,
    MatPaginatorModule, MatFormFieldModule, MatDialogModule, AddUserComponent],
  templateUrl: './user.component.html',
  styleUrl: './user.component.sass'
})
export class UserComponent extends TableComponent<User> {

  override newEmptyTableUi(): TableUi {
    return new TableUi("user.error.");
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = "user.";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("firstName", labelKeyPrefix + "firstName"));
    result.push(new TableColumnUi("lastName", labelKeyPrefix + "lastName"));
    result.push(new TableColumnUi("username", labelKeyPrefix + "username"));
    result.push(new TableColumnUi("email", labelKeyPrefix + "email"));
    result.push(new TableColumnUi("activated", labelKeyPrefix + "activated"));
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
    return environment.apiUrl.user;
  }

  override getDeleteConfirmContent(item: User): string {
    return this.languageService.formatLanguage("user.deleteConfirmContent", [item.displayName]);
  }

  override getDeleteConfirmTitle(item: User): string {
    return this.languageService.formatLanguage("user.deleteConfirmTitle", []);
  }

  override createNewItem(): User {
    return new User();
  }

  override permissionResourceName(): string {
    return "userprofile";
  }
}
