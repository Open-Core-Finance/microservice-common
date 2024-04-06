import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { GlAccount } from 'src/app/classes/accounts/GlAccount';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { UiFormComplexInput, UiFormDivider, UiFormInput, UiFormItem, UiFormSelect, UiFormTextarea, UiSelectItem } from 'src/app/classes/ui/UiFormInput';
import { environment } from 'src/environments/environment';
import { LanguageService } from 'src/app/services/language.service';
import { CommonService } from 'src/app/services/common.service';
import { RestService } from 'src/app/services/rest.service';
import { HttpClient } from '@angular/common/http';
import { OrganizationService } from 'src/app/services/organization.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { GlProductService, ProductCategoryService, ProductTypeService } from 'src/app/services/product/gl.product.service';
import { GlProduct } from 'src/app/classes/products/GlProduct';
import { Subscription } from 'rxjs';
import { ProductCategory, ProductCategoryType } from 'src/app/classes/products/ProductCategory';
import { ProductType } from 'src/app/classes/products/ProductType';
import { CurrencyModule } from 'src/app/generic-component/CurrencyModule';

@Component({
  selector: 'app-add-gl-account',
  standalone: true,
  imports: [CommonModule, SharedModule, CurrencyModule],
  templateUrl: './add-gl-account.component.html',
  styleUrl: './add-gl-account.component.sass'
})
export class AddGlAccountComponent extends GeneralEntityAddComponent<GlAccount> implements OnDestroy, OnInit {

  glProducts: GlProduct[] = [];
  glProductSubscription: Subscription | undefined;
  productCurrencies: string[] = [];
  previousProductId: String | undefined;
  protected valueChangeSubscription: Subscription | undefined;

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService, private glProductService: GlProductService) {
      super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
  }

  ngOnInit(): void {
    this.glProductSubscription?.unsubscribe();
    this.glProductSubscription = this.glProductService.entityListSubject.subscribe( glProducts => {
      this.glProducts = glProducts ? glProducts : [];
      this.updateSelectItem("productId");
    });
    this.valueChangeSubscription?.unsubscribe();
    this.valueChangeSubscription = this.addForm.valueChanges.subscribe( v => this.valueChanged(v));
  }
  valueChanged(v: any): void {
    let selectedProductId = v.productId;
    if (this.previousProductId != selectedProductId) {
      this.previousProductId = selectedProductId;
      this.productCurrencies = [];
      if (this.glProducts) {
        for (let i = 0; i < this.glProducts.length; i++) {
          const glProduct = this.glProducts[i];
          if (glProduct.id == selectedProductId) {
            this.productCurrencies = glProduct.currencies;
          }
        }
      }
    }
  }

  ngOnDestroy(): void {
    this.glProductSubscription?.unsubscribe();
    this.valueChangeSubscription?.unsubscribe();
  }

  protected override buildFormItems(): UiFormItem[] {
    const formItems: UiFormItem[] = [];
    const prefix = "glaccount.";
    // ID auto generate
    formItems.push(new UiFormInput(prefix + "name", "name"));
    formItems.push(new UiFormSelect(prefix + "product", this.buildListSelection("productId"), "productId"));
    formItems.push(new UiFormDivider());
    var item = new UiFormTextarea(prefix + "description", "description");
    item.additionClass = 'full-width';
    formItems.push(item);
    formItems.push(new UiFormDivider());
    // Currencies
    formItems.push(new UiFormComplexInput("currencies", "supportedCurrencies"));
    // Return
    return formItems;
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.glAccount;
  }

  protected override validateFormData(formData: any): void {
    const errors = this.message['error'];
    if (this.commonService.isNullOrEmpty(formData.name)) {
      errors.push("name_empty")
    }
  }

  protected override newEmptyEntity(): GlAccount {
    return new GlAccount();
  }

  protected override buildListSelection(selectName: string): UiSelectItem[] {
    if (selectName == 'productId') {
      return this.glProducts ? this.glProducts.map( m => ({
        selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    }
    return super.buildListSelection(selectName);
  }

  protected override get additionalFormGroupElement(): any {
    return {
      supportedCurrencies: new FormControl<string[]>([])
    };
  }
}
