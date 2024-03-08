import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { Currency } from 'src/app/classes/Currency';
import { Subscription } from 'rxjs';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { LanguageService } from 'src/app/services/language.service';
import { RestService } from 'src/app/services/rest.service';
import { HttpClient } from '@angular/common/http';
import { CommonService } from 'src/app/services/common.service';
import { EntitiesService } from 'src/app/services/EntitiesService';
import { environment } from 'src/environments/environment';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { OrganizationService } from 'src/app/services/organization.service';
import { UiFormInput, UiFormItem, UiFormSelect } from 'src/app/classes/ui/UiFormInput';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-add-curreny',
  templateUrl: './add-curreny.component.html',
  styleUrl: './add-curreny.component.sass'
})
export class AddCurrenyComponent extends GeneralEntityAddComponent<Currency> implements OnDestroy {

  currencies: Currency[] = [];
  currencySubscription: Subscription | undefined;

  addCurrencyForm = new FormGroup({
    index: new FormControl(0),
    id: new FormControl(""),
    name: new FormControl('', {nonNullable: true}),
    symbol: new FormControl("", {nonNullable: true}),
    decimalMark: new FormControl("", {nonNullable: true}),
    symbolAtBeginning: new FormControl(false, {nonNullable: true})
  });

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService, private entitiesService: EntitiesService) {
      super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
      this.currencySubscription?.unsubscribe();
      this.currencySubscription = entitiesService.entitySubjectMap.get(EntitiesService.ENTITY_TYPE_CURRENCY)?.subscribe(
         currencies => this.currencies = currencies
      );

      // Form UI
      this.formItems = [];
      const nameInput = new UiFormInput("currency", "name");
      this.formItems.push(nameInput);
      const symbolInput = new UiFormInput("currencySymbol", "symbol");
      this.formItems.push(symbolInput);

      const decimalMarkInput = new UiFormSelect("decimalMark", [ { labelKey: "decimalMarkPeriod", selectValue: "." },
        { labelKey: "decimalMarkComma", selectValue: "," } ], "decimalMark");
      this.formItems.push(decimalMarkInput);
      const currencySymbolPosisionInput = new UiFormSelect("currencySymbolPosision", [ { labelKey: "currencySymbolPosision_true", selectValue: true },
        { labelKey: "currencySymbolPosision_false", selectValue: false } ], "symbolAtBeginning");
      this.formItems.push(currencySymbolPosisionInput);
  }

  ngOnDestroy(): void {
    this.currencySubscription?.unsubscribe();
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.currency;
  }
  protected override getAddForm(): FormGroup<any> {
    return this.addCurrencyForm;
  }
  protected override validateFormData(formData: any): void {
    if (this.commonService.isNullOrEmpty(formData.name)) {
      this.message['error'].push("name_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.symbol)) {
      this.message['error'].push("symbol_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.decimalMark)) {
      this.message['error'].push("decimal_mark_empty")
    }
  }
  protected override newEmptyEntity(): Currency {
    return new Currency();
  }
}
