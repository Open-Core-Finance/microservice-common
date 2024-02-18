import { Component } from '@angular/core';
import { RateSource } from 'src/app/classes/products/RateSource';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-rate-source',
  templateUrl: './rate-source.component.html',
  styleUrl: './rate-source.component.sass'
})
export class RateSourceComponent extends TableComponent<RateSource> {

  override permissionResourceName(): string {
    return "ratesource";
  }

  override buildTableColumns(): string[] {
    return ["index", "id", "name", "type", "note", "action"];
  }

  override ngAfterViewInit(): void {
    super.ngAfterViewInit();
    const order = new UiOrderEvent();
    order.active = "id";
    order.direction = "asc";
    this.changeOrder({ order });
  }

  getServiceUrl() {
    return environment.apiUrl.rateSource;
  }

  override getDeleteConfirmContent(item: RateSource): string {
    return this.languageService.formatLanguage("rateSource.deleteConfirmContent", [item.name]);
  }

  override getDeleteConfirmTitle(item: RateSource): string {
    return this.languageService.formatLanguage("rateSource.deleteConfirmTitle", []);
  }

  override createNewItem(): RateSource {
    return new RateSource();
  }
}
