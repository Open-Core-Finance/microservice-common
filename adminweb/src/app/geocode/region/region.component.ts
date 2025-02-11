import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Region } from 'src/app/classes/geocode/Region';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { AppComponent } from 'src/app/app.component';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';
import { environment } from 'src/environments/environment';
import { AddRegionComponent } from '../add-region/add-region.component';

@Component({
    selector: 'app-region',
    imports: [CommonModule, SharedModule, AddRegionComponent],
    templateUrl: './region.component.html',
    styleUrl: './region.component.sass'
})
export class RegionComponent extends TableComponent<Region> {

  override permissionResourceName(): string {
    return "region";
  }

  parent: AppComponent | undefined;

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("translations", labelKeyPrefix + "translations"));
    result.push(new TableColumnUi("enabled", labelKeyPrefix + "enabled"));
    result.push(new TableColumnUi("wikiDataId", labelKeyPrefix + "wikiDataId"));
    result.push(new TableColumnUi("lastModifiedDate", labelKeyPrefix + "lastModifiedDate", {
      dateFormat: "yyyy-MM-dd hh:mm:ss"
    }));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.region;
  }

  override createNewItem(): Region {
    return new Region();
  }

  override get indexColumnLabelKey(): string {
    return "";
  }
}
