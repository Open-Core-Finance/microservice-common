import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ExchangeRate } from 'src/app/classes/products/ExchangeRate';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-add-exchange-rate',
  templateUrl: './add-exchange-rate.component.html',
  styleUrl: './add-exchange-rate.component.sass'
})
export class AddExchangeRateComponent extends GeneralEntityAddComponent<ExchangeRate>{

  addCurrencyForm = new FormGroup({
    index: new FormControl(0),
    id: new FormControl(""),
    name: new FormControl('', {nonNullable: true}),
    sellRate: new FormControl(0.0, {nonNullable: true}),
    buyRate: new FormControl(0.0, {nonNullable: true})
  });

  protected override getServiceUrl(): string {
    return environment.apiUrl.exchangeRate;
  }

  protected override getAddForm(): FormGroup<any> {
    return this.addCurrencyForm;
  }

  protected override validateFormData(formData: any): void {
    if (this.commonService.isNullOrEmpty(formData.name)) {
      this.message['error'].push("name_empty")
    }
    if (formData.sellRate == null || formData.sellRate <= 0) {
      this.message['error'].push("sell_rate_error")
    }
    if (formData.buyRate == null || formData.buyRate <= 0) {
      this.message['error'].push("buy_rate_error")
    }
  }

  protected override newEmptyEntity(): ExchangeRate {
    return new ExchangeRate();
  }
}
