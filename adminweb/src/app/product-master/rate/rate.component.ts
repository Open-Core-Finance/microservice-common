import { Component } from '@angular/core';
import { UiOrderEvent } from 'src/app/classes/UiOrderEvent';
import { Rate } from 'src/app/classes/products/Rate';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-rate',
  templateUrl: './rate.component.html',
  styleUrl: './rate.component.sass'
})
export class RateComponent extends TableComponent<Rate> {

  override buildTableColumns(): string[] {
   return ["index", "id", "rateValue", "validFrom", "note", "rateSource", "action"];
  }

  override ngAfterViewInit(): void {
   super.ngAfterViewInit();
   const order = new UiOrderEvent();
   order.active = "id";
   order.direction = "asc";
   this.changeOrder({ order });
  }

  getServiceUrl() {
   return environment.apiUrl.rate;
  }

  override getDeleteConfirmContent(item: Rate): string {
   return this.languageService.formatLanguage("rate.deleteConfirmContent", [item.index]);
  }

  override getDeleteConfirmTitle(item: Rate): string {
   return this.languageService.formatLanguage("rate.deleteConfirmTitle", []);
  }

  override createNewItem(): Rate {
   return new Rate();
  }
}
