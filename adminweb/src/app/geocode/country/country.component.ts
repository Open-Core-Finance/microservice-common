import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { AppComponent } from 'src/app/app.component';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';
import { environment } from 'src/environments/environment';
import { AddCountryComponent } from '../add-country/add-country.component';
import { Country } from 'src/app/classes/geocode/Country';

@Component({
    selector: 'app-country',
    imports: [CommonModule, SharedModule, AddCountryComponent],
    templateUrl: './country.component.html',
    styleUrl: './country.component.sass'
})
export class CountryComponent extends TableComponent<Country> {

  override permissionResourceName(): string {
    return "country";
  }

  override get localizePrefix(): string {
    return "countries";
  }

  parent: AppComponent | undefined;

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("regionName", labelKeyPrefix + "region"));
    result.push(new TableColumnUi("subregionName", labelKeyPrefix + "subRegion"));
    result.push(new TableColumnUi("enabled", labelKeyPrefix + "enabled"));
    result.push(new TableColumnUi("wikiDataId", labelKeyPrefix + "wikiDataId"));
    result.push(new TableColumnUi("lastModifiedDate", labelKeyPrefix + "lastModifiedDate", {
      dateFormat: "yyyy-MM-dd hh:mm:ss"
    }));
    result.push(new TableColumnUi("phoneCode", labelKeyPrefix + "phoneCode"));
    result.push(new TableColumnUi("iso3", labelKeyPrefix + "iso3"));
    result.push(new TableColumnUi("iso2", labelKeyPrefix + "iso2"));
    result.push(new TableColumnUi("nativePeople", labelKeyPrefix + "nativePeople"));
    result.push(new TableColumnUi("translations", labelKeyPrefix + "translations"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.country;
  }

  override createNewItem(): Country {
    return new Country();
  }

  override get indexColumnLabelKey(): string {
    return "";
  }
}