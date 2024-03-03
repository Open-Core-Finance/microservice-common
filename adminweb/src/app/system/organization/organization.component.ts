import { Component } from '@angular/core';
import { Organization } from 'src/app/classes/Organization';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { environment } from 'src/environments/environment';
import {AppComponent} from "../../app.component";
import { TableColumnUi, TableUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
  selector: 'app-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.sass']
})
export class OrganizationComponent extends TableComponent<Organization> {

  override permissionResourceName(): string {
    return "organization";
  }

  parent: AppComponent | undefined;

  override newEmptyTableUi(): TableUi {
    return new TableUi("organization.error.");
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = "organization.";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("iconUrl", labelKeyPrefix + "iconUrl"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("country", "country"));
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
    return environment.apiUrl.organization;
  }

  override getDeleteConfirmContent(item: Organization): string {
    return this.languageService.formatLanguage("organization.deleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: Organization): string {
    return this.languageService.formatLanguage("organization.deleteConfirmTitle", []);
  }

  override createNewItem(): Organization {
    return new Organization();
  }

  viewOrganizationClick(organization: Organization) {
    this.organizationService.organization = organization;
    const urlToNavigate = "/" + environment.frontEndUrl.organizationDetails;
    this.router.navigateByUrl(urlToNavigate);
    this.parent?.openMenu();
  }
}
