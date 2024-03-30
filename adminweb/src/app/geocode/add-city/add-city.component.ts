import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit } from '@angular/core';
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
import { CountryService, RegionService, StateService } from 'src/app/services/geocode.service';
import { Country } from 'src/app/classes/geocode/Country';
import { City } from 'src/app/classes/geocode/City';
import { State } from 'src/app/classes/geocode/State';

@Component({
  selector: 'app-add-city',
  standalone: true,
  imports: [CommonModule, SharedModule],
  templateUrl: './add-city.component.html',
  styleUrl: './add-city.component.sass'
})
export class AddCityComponent extends GeneralEntityAddComponent<City> implements OnDestroy, OnInit {

  private states: State[] = [];
  private statesSubscription: Subscription | undefined;
  private countries: Country[] = [];
  private countriesSubscription: Subscription | undefined;
  private filteredStates: State[] = [];
  private valueChangeSubscription: Subscription | undefined;
  private previousCountryId: number | null = null;

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService, private regionService: RegionService,
    private stateService: StateService, private countryService: CountryService) {
      super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
  }

  ngOnInit(): void {
    this.statesSubscription?.unsubscribe();
    this.statesSubscription = this.stateService.entityListSubject.subscribe( items => {
      this.states = items;
      this.valueChanged(this.addForm.value);
    });
    this.countriesSubscription?.unsubscribe();
    this.countriesSubscription = this.countryService.entityListSubject.subscribe( items => {
      this.countries = items;
      this.updateSelectItem("countryId");
    });
    this.valueChangeSubscription?.unsubscribe();
    this.valueChangeSubscription = this.addForm.valueChanges.subscribe( v => this.valueChanged(v));
  }

  ngOnDestroy(): void {
    this.statesSubscription?.unsubscribe();
    this.countriesSubscription?.unsubscribe();
    this.valueChangeSubscription?.unsubscribe();
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.country;
  }

  protected override validateFormData(formData: any): void {
    const errors = this.message['error'];
    if (this.commonService.isNullOrEmpty(formData.name)) {
      errors.push("name_empty")
    }
  }

  protected override newEmptyEntity(): City {
    return new City();
  }

  protected override buildFormItems(): UiFormItem[] {
    const formItems: UiFormItem[] = [];
    const prefix = "cities.";
    formItems.push(new UiFormInput(prefix + "name", "name"));
    formItems.push(new UiFormSelect(prefix + "country", this.buildListSelection("countryId"), "countryId"));
    formItems.push(new UiFormSelect(prefix + "state", this.buildListSelection("stateId"), "stateId"));
    formItems.push(new UiFormCheckbox(prefix + "enabled", "enabled"));
    formItems.push(new UiFormInput(prefix + "wikiDataId", "wikiDataId"));
    formItems.push(new UiFormInput(prefix + "latitude", "latitude", "number"));
    formItems.push(new UiFormInput(prefix + "longitude", "longitude", "number"));
    
    formItems.push(new UiFormDivider());
    let item: UiFormItem = new UiFormTextarea(prefix + "translations", "translations");
    item.additionClass = "full-width";
    formItems.push(item);

    return formItems;
  }

  protected override buildListSelection(selectName: string): UiSelectItem[] {
    if (selectName == 'stateId') {
      return this.states ? this.states.map( m => ({
        selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    } else if (selectName == 'countryId') {
      return this.countries ? this.countries.map( m => ({
        selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    }
    return super.buildListSelection(selectName);
  }

  @Input() override set addingItem(item: City| null) {
    if (item) {
      this.previousCountryId = item.countryId;
    } else {
      this.previousCountryId = null;
    }
    super.addingItem = item;
  }

  valueChanged(value: any) {
    const selectedCountry = value.countryId;
    if (this.previousCountryId != selectedCountry) {
      this.previousCountryId = selectedCountry;
      this.refreshSubRegions();
      if (this.filteredStates.length > 0) {
        this.addForm.patchValue({stateId: this.filteredStates[0].id})
      }
    } else if (this.filteredStates.length < 1 && this.previousCountryId != null) {
      this.refreshSubRegions();
    }
  }

  refreshSubRegions() {
    this.filteredStates = [];
    for (let s of this.states) {
      if (s.countryId == this.previousCountryId) {
        this.filteredStates.push(s);
      }
    }
    this.updateSelectItem("stateId");
  }
}