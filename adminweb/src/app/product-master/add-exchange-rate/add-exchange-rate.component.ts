import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ExchangeRate } from 'src/app/classes/products/ExchangeRate';
import { UiFormInput, UiFormItem, UiFormSelect, UiSelectItem } from 'src/app/classes/ui/UiFormInput';
import { environment } from 'src/environments/environment';
import { LanguageService } from 'src/app/services/language.service';
import { CommonService } from 'src/app/services/common.service';
import { RestService } from 'src/app/services/rest.service';
import { HttpClient } from '@angular/common/http';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { CustomDateFormat } from 'src/app/classes/DateFormat';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { OrganizationService } from 'src/app/services/organization.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { CurrencyService } from 'src/app/services/currency.service';
import { Currency } from 'src/app/classes/Currency';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-add-exchange-rate',
  templateUrl: './add-exchange-rate.component.html',
  styleUrl: './add-exchange-rate.component.sass'
})
export class AddExchangeRateComponent extends GeneralEntityAddComponent<ExchangeRate>{

  addForm = this.formBuilder.group(new ExchangeRate());

  currencies: Currency[] = [];
  currenciesSubscription: Subscription | undefined;

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService, private currencyService: CurrencyService) {
      super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
      this.currenciesSubscription?.unsubscribe();
      this.currenciesSubscription = this.currencyService.currenciesSubject.subscribe(currencies => {
        this.currencies = currencies ? currencies : [];
        this.updateSelectItem("fromCurrency");
        this.updateSelectItem("toCurrency");
      });
  }

  protected override buildFormItems(): UiFormItem[] {
    const formItems: UiFormItem[] = [];
    const prefix = "exchangeRate.";
    formItems.push(new UiFormSelect(prefix + "fromCurrency", this.buildListSelection("fromCurrency"), "fromCurrency"));
    formItems.push(new UiFormSelect(prefix + "toCurrency", this.buildListSelection("toCurrency"), "toCurrency"));
    var input = new UiFormInput(prefix + "sellRate", "sellRate");
    input.inputType = "number";
    formItems.push(input);
    input = new UiFormInput(prefix + "buyRate", "buyRate");
    input.inputType = "number";
    formItems.push(input);
    input = new UiFormInput(prefix + "margin", "margin");
    input.inputType = "number";
    formItems.push(input);
    return formItems;
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.exchangeRate;
  }

  protected override getAddForm(): FormGroup<any> {
    return this.addForm;
  }

  protected override validateFormData(formData: any): void {
    if (this.commonService.isNullOrEmpty(formData.fromCurrency)) {
      this.message['error'].push("from_currency_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.toCurrency)) {
      this.message['error'].push("to_currency_empty")
    }
    if (formData.sellRate == null || formData.sellRate <= 0) {
      this.message['error'].push("sell_rate_error")
    }
    if (formData.buyRate == null || formData.buyRate <= 0) {
      this.message['error'].push("buy_rate_error")
    }
    var id = {fromCurrency: formData.fromCurrency, toCurrency: formData.toCurrency};
    formData.id = JSON.stringify(id);
  }

  protected override newEmptyEntity(): ExchangeRate {
    return new ExchangeRate();
  }

  protected override buildListSelection(selectName: string): UiSelectItem[] {
    if (selectName == 'fromCurrency' || selectName == 'toCurrency') {
      return this.currencies ? this.currencies.map( c => ({selectValue: c.id, labelKey: c.id} as UiSelectItem)) : [];
    }
    return [];
  }
}
