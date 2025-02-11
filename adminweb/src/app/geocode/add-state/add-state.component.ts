import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { environment } from 'src/environments/environment';
import { FormBuilder, FormGroup } from '@angular/forms';
import { UiFormDivider, UiFormInput, UiFormItem, UiFormSelect, UiSelectItem } from 'src/app/classes/ui/UiFormInput';
import { UiFormTextarea } from 'src/app/classes/ui/UiFormInput';
import { UiFormCheckbox } from 'src/app/classes/ui/UiFormInput';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { LanguageService } from 'src/app/services/language.service';
import { CommonService } from 'src/app/services/common.service';
import { RestService } from 'src/app/services/rest.service';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { Subscription } from 'rxjs';
import { CountryService} from 'src/app/services/geocode.service';
import { Country } from 'src/app/classes/geocode/Country';
import { State } from 'src/app/classes/geocode/State';

@Component({
    selector: 'app-add-state',
    imports: [CommonModule, SharedModule],
    templateUrl: './add-state.component.html',
    styleUrl: './add-state.component.sass'
})
export class AddStateComponent extends GeneralEntityAddComponent<State> implements OnDestroy, OnInit {

  private countries: Country[] = [];
  private countriesSubscription: Subscription | undefined;

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService, private countryService: CountryService) {
      super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
  }

  ngOnInit(): void {
    this.countriesSubscription?.unsubscribe();
    this.countriesSubscription = this.countryService.entityListSubject.subscribe( items => {
      this.countries = items;
      this.updateSelectItem("countryId");
    });
  }

  ngOnDestroy(): void {
    this.countriesSubscription?.unsubscribe();
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.state;
  }

  protected override validateFormData(formData: any): void {
    const errors = this.message['error'];
    if (this.commonService.isNullOrEmpty(formData.name)) {
      errors.push("name_empty")
    }
    if (!formData.translations || formData.translations.trim() == '' || formData.translations == '{}') {
      formData.translations = null;
    }
  }

  protected override newEmptyEntity(): State {
    return new State();
  }

  protected override buildFormItems(): UiFormItem[] {
    const formItems: UiFormItem[] = [];
    const prefix = "states.";
    formItems.push(new UiFormInput(prefix + "name", "name"));
    formItems.push(new UiFormSelect(prefix + "country", this.buildListSelection("countryId"), "countryId"));
    formItems.push(new UiFormCheckbox(prefix + "enabled", "enabled"));
    formItems.push(new UiFormInput(prefix + "wikiDataId", "wikiDataId"));
    formItems.push(new UiFormInput(prefix + "iso2", "iso2"));
    formItems.push(new UiFormInput(prefix + "type", "type"));
    formItems.push(new UiFormInput(prefix + "latitude", "latitude", "number"));
    formItems.push(new UiFormInput(prefix + "longitude", "longitude", "number"));
    formItems.push(new UiFormInput(prefix + "countryCode", "countryCode"));
    formItems.push(new UiFormInput(prefix + "fipsCode", "fipsCode"));

    formItems.push(new UiFormDivider());
    let item: UiFormItem = new UiFormTextarea(prefix + "translations", "translations");
    item.additionClass = "full-width";
    formItems.push(item);
    return formItems;
  }

  protected override buildListSelection(selectName: string): UiSelectItem[] {
    if (selectName == 'countryId') {
      return this.countries ? this.countries.map( m => ({
        selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    }
    return super.buildListSelection(selectName);
  }
}