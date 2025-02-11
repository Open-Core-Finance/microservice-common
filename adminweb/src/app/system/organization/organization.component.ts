import { Component } from '@angular/core';
import { Organization } from 'src/app/classes/Organization';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { environment } from 'src/environments/environment';
import {AppComponent} from "../../app.component";
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
    selector: 'app-organization',
    templateUrl: './organization.component.html',
    styleUrls: ['./organization.component.sass'],
    standalone: false
})
export class OrganizationComponent extends TableComponent<Organization> {

  override permissionResourceName(): string {
    return "organization";
  }

  parent: AppComponent | undefined;

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("iconUrl", labelKeyPrefix + "iconUrl"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("country", "country"));
    result.push(new TableColumnUi("phoneNumber", "phoneNumber"));
    result.push(new TableColumnUi("email", "email"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.organization;
  }

  override createNewItem(): Organization {
    return new Organization();
  }

  viewOrganizationClick(organization: Organization) {
    if (organization != undefined) {
      this.organizationService.organization = organization;
      const urlToNavigate = "/" + environment.frontEndUrl.organizationDetails;
      this.router.navigateByUrl(urlToNavigate);
      this.parent?.openMenu();
    }
  }
}
