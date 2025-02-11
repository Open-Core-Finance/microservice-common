import { Component } from '@angular/core';
import { RateSource } from 'src/app/classes/products/RateSource';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { environment } from 'src/environments/environment';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';

@Component({
    selector: 'app-rate-source',
    templateUrl: './rate-source.component.html',
    styleUrl: './rate-source.component.sass',
    standalone: false
})
export class RateSourceComponent extends TableComponent<RateSource> {

  override permissionResourceName(): string {
    return "ratesource";
  }

  override get localizePrefix(): string {
    return "rateSource";
  }

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("type", labelKeyPrefix + "type"));
    result.push(new TableColumnUi("note", labelKeyPrefix + "note"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.rateSource;
  }

  override createNewItem(): RateSource {
    return new RateSource();
  }
}
