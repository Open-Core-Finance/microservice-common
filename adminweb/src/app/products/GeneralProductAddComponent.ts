import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit } from "@angular/core";
import { Product, ProductNewAccountSettingType } from "../classes/products/Product";
import { ProductCategory, ProductCategoryType } from "../classes/products/ProductCategory";
import { ProductType } from "../classes/products/ProductType";
import { GeneralEntityAddComponent } from "../generic-component/GeneralEntityAddComponent";
import { GeneralApiResponse } from "../classes/GeneralApiResponse";
import { environment } from "src/environments/environment";
import { Subscription } from "rxjs";
import { Organization } from "../classes/Organization";
import { Currency } from "../classes/Currency";
import { CurrencyService } from "../services/currency.service";
import { LanguageService } from "../services/language.service";
import { RestService } from "../services/rest.service";
import { CommonService } from "../services/common.service";
import { HttpClient } from "@angular/common/http";
import { FormBuilder } from "@angular/forms";
import { OrganizationService } from "../services/organization.service";
import { AccountState } from "../classes/accounts/AccountState";
import { CurrencyBasesValue } from "../classes/products/ValueConstraint";
import { AuthenticationService } from "../services/authentication.service";

@Component({
    template: ''
})
export abstract class GeneralProductAddComponent<T extends Product> extends GeneralEntityAddComponent<T> implements OnInit, AfterViewInit, OnDestroy {

    protected productCategories: ProductCategory[] = [];
    protected productTypes: ProductType[] = [];
    protected newAccountIdTypeEnum = ProductNewAccountSettingType;
    protected organizationSubscription: Subscription | undefined;
    protected lastOrganization: Organization | null = null;
    protected currencies: Currency[] = [];
    protected currenciesSubscription: Subscription | undefined;
    protected accountStateEnum = AccountState;
    protected allAccountStates = Object.keys(AccountState);
    protected currenciesToDisplay: Currency[] = [];

    constructor(public override languageService: LanguageService, protected override commonService: CommonService,
        protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
        protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
        protected override authenticationService: AuthenticationService, protected currencyService: CurrencyService) {
        super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
    }

    protected override validateFormData(formData: any): void {
        var errs = this.message['error'] as string[];
        if (this.commonService.isNullOrEmpty(formData.name)) {
            errs.push("product_name_empty");
        }
        if (this.commonService.isNullOrEmpty(formData.category)) {
            errs.push("product_category_empty");
        }
        if (this.commonService.isNullOrEmpty(formData.type)) {
            errs.push("type_empty");
        }
        if (this.commonService.isNullOrEmpty(formData.newAccountSetting.type)) {
            errs.push("new_account_type_empty");
        }
        if (formData.currencies.length < 1) {
            errs.push("currencies_empty");
        }
    }

    protected abstract getProductCategoryType(): ProductCategoryType;

    ngOnInit(): void {
        let headers = this.restService.initRequestHeaders();
        this.http.get<GeneralApiResponse>(environment.apiUrl.productCategory + "/", { headers, params: {
            type: this.getProductCategoryType()
        }}).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.productCategories = (data.result as ProductCategory[]);
                } else this.restService.handleRestError(data, this.message)
            }, error: (data: any) => this.restService.handleRestError(data, this.message)
        });
        this.http.get<GeneralApiResponse>(environment.apiUrl.productType + "/", { headers, params: {
            type: this.getProductCategoryType()
        } }).subscribe({
            next: (data: GeneralApiResponse) => {
                if (data.status === 0) {
                    this.productTypes = (data.result as ProductType[]);
                } else this.restService.handleRestError(data, this.message)
            }, error: (data: any) => this.restService.handleRestError(data, this.message)
        });
        this.organizationSubscription?.unsubscribe();
        this.organizationSubscription = this.organizationService.organizationSubject.subscribe( org => {
            this.lastOrganization = org;
            const addForm = this.getAddForm();
            if (!addForm.value.currencies || addForm.value.currencies.length < 1) {
                addForm.controls['currencies'].setValue([org?.currency.id]);
                this.currenciesChanged();
            }
        });
        this.currenciesSubscription?.unsubscribe();
        this.currenciesSubscription = this.currencyService.currenciesSubject.subscribe( c => {
            this.currencies = c;
            this.currenciesChanged();
        });
    }

    ngAfterViewInit(): void {
    }

    ngOnDestroy(): void {
        this.organizationSubscription?.unsubscribe();
    }

    protected override afterBindingEnityToForm(isNew: boolean): void {
        const form = this.getAddForm();
        var formValue = form.value;
        if (isNew && this.lastOrganization) {
            if (!formValue.currencies || formValue.currencies.length < 1) {
                form.controls['currencies'].setValue([this.lastOrganization?.currency.id]);
                this.currenciesChanged();
            }
        }
    }

    protected currenciesChanged() {
        this.currenciesToDisplay = [];
        const form = this.getAddForm();
        const supportedCurrencies = form.value.currencies ? form.value.currencies : [];
        for (let currency of this.currencies) {
            for(let  i = 0; i < supportedCurrencies.length; i++) {
                const c = supportedCurrencies[i];
                if (c == currency.id) {
                    this.currenciesToDisplay.push(currency);
                    break;
                }
            }
        }
    }

    protected cleanUpConstraints<T extends CurrencyBasesValue>(constraints: T[]) {
        for (let i = 0; i < constraints.length; i++) {
          const constraint = constraints[i];
          var found = false;
          for(let  j = 0; j < this.currenciesToDisplay.length; j++) {
            const currency = this.currenciesToDisplay[j];
            if (constraint.currencyId == currency.id) {
              found = true;
              break;
            }
          }
          if (!found) {
            constraints.splice(i, 1);
            i--;
          }
        }
      }
    
      protected addMissingConstraints<T extends CurrencyBasesValue>(constraints: T[], newItem: T) {
        for(let  j = 0; j < this.currenciesToDisplay.length; j++) {
          const currency = this.currenciesToDisplay[j];
          var found = false;
          for (let i = 0; i < constraints.length; i++) {
            const constraint = constraints[i];
            if (constraint.currencyId == currency.id) {
              found = true;
              break;
            }
          }
          if (!found) {
            newItem.currencyId = currency.id;
            constraints.push(newItem);
          }
        }
      }
}