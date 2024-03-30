import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { IndividualCustomer } from 'src/app/classes/customers/IndividualCustomer';
import { environment } from 'src/environments/environment';
import { FormBuilder } from '@angular/forms';
import { ExpansionPanelInputGroup, UiFormBigHeader, UiFormCheckbox, UiFormComplexInput, UiFormDate, UiFormInput, UiFormItem, UiFormSelect, UiSelectItem } from 'src/app/classes/ui/UiFormInput';
import { UiFormDivider } from 'src/app/classes/ui/UiFormInput';
import { Gender } from 'src/app/classes/Gender';
import { MaritalStatus } from 'src/app/classes/customers/MaritalStatus';
import { LanguageService } from 'src/app/services/language.service';
import { RestService } from 'src/app/services/rest.service';
import { HttpClient } from '@angular/common/http';
import { CommonService } from 'src/app/services/common.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { State } from 'src/app/classes/geocode/State';
import { Country } from 'src/app/classes/geocode/Country';
import { Subscription } from 'rxjs';
import { CityService, CountryService, StateService } from 'src/app/services/geocode.service';
import { City } from 'src/app/classes/geocode/City';
import { GeneralApiResponse } from 'src/app/classes/GeneralApiResponse';
import { Nationality } from 'src/app/classes/customers/Nationality';
import { CustomerIdentityType } from 'src/app/classes/customers/CustomerIdentityType';

@Component({
  selector: 'app-add-individual-customer',
  standalone: true,
  imports: [CommonModule, SharedModule],
  templateUrl: './add-individual-customer.component.html',
  styleUrl: './add-individual-customer.component.sass'
})
export class AddIndividualCustomerComponent extends GeneralEntityAddComponent<IndividualCustomer> implements OnDestroy, OnInit {

  private states: State[] = [];
  private statesSubscription: Subscription | undefined;
  private countries: Country[] = [];
  private countriesSubscription: Subscription | undefined;
  private valueChangeSubscription: Subscription | undefined;

  private filteredStates: State[] = [];
  private previousCountryId: number | null = null;
  private filteredCities: City[] = [];
  private previousStateId: number | null = null;

  private filteredMailingStates: State[] = [];
  private previousMailingCountryId: number | null = null;
  private filteredMailingCities: City[] = [];
  private previousMailingStateId: number | null = null;

  nationalityIdentityTypeItems: UiSelectItem[] = Object.keys(CustomerIdentityType).map( t =>
    ({labelKey: "nationality.identityType_" + t, selectValue: t} as UiSelectItem)
  );

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService,
    private stateService: StateService, private countryService: CountryService, private cityService: CityService) {
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
      this.updateSelectItem("mailingCountryId");
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
    return environment.apiUrl.individualCustomer;
  }

  protected override validateFormData(formData: any): void {
    const errors = this.message['error'];
    if (this.commonService.isNullOrEmpty(formData.contactEmail)) {
      errors.push("contact_email_empty")
    }
  }

  protected override newEmptyEntity(): IndividualCustomer {
    return new IndividualCustomer();
  }

  protected override buildFormItems(): UiFormItem[] {
    const that = this;
    const formItems: UiFormItem[] = [];
    let prefix = "individualCustomer.";
    // Generic info
    formItems.push(new UiFormInput(prefix + "title", "title"));
    formItems.push(new UiFormInput(prefix + "firstName", "firstName"));
    formItems.push(new UiFormInput(prefix + "middleName", "middleName"));
    formItems.push(new UiFormInput(prefix + "lastName", "lastName"));
    formItems.push(new UiFormSelect(prefix + "gender",
      Object.keys(Gender).map( s => ({labelKey: "user.gender_" + s, selectValue: s} as UiSelectItem))
    , "gender"));
    formItems.push(new UiFormInput(prefix + "cisNumber", "cisNumber", "text"));
    formItems.push(new UiFormInput(prefix + "placeOfBirth", "placeOfBirth"));
    formItems.push(new UiFormDate(prefix + "dob", "dob"));
    formItems.push(new UiFormSelect(prefix + "maritalStatus",
      Object.keys(MaritalStatus).map( s => ({labelKey: "maritalStatus." + s, selectValue: s} as UiSelectItem))
    , "maritalStatus"));
    
    // Contact info
    formItems.push(new UiFormDivider());
    formItems.push(new UiFormBigHeader(() => that.languageService.formatLanguage(prefix + "contactInfo")));
    formItems.push(new UiFormInput(prefix + "contactPhone", "contactPhone"));
    formItems.push(new UiFormInput(prefix + "contactHomePhone", "contactHomePhone"));
    formItems.push(new UiFormInput(prefix + "contactEmail", "contactEmail"));
    formItems.push(new UiFormInput(prefix + "contactCompanyPhone", "contactCompanyPhone"));
    formItems.push(new UiFormSelect(prefix + "country", this.buildListSelection("countryId"), "countryId"));
    formItems.push(new UiFormSelect(prefix + "state", this.buildListSelection("stateId"), "stateId"));
    formItems.push(new UiFormSelect(prefix + "city", this.buildListSelection("cityId"), "cityId"));
    formItems.push(new UiFormInput(prefix + "district", "district"));
    formItems.push(new UiFormInput(prefix + "streetAddressLine1", "streetAddressLine1"));
    formItems.push(new UiFormInput(prefix + "streetAddressLine2", "streetAddressLine2"));
    formItems.push(new UiFormInput(prefix + "zipPostalCode", "zipPostalCode"));

    // Mailing info
    formItems.push(new UiFormDivider());
    formItems.push(new UiFormBigHeader(() => that.languageService.formatLanguage(prefix + "mailingInfo")));
    formItems.push(new UiFormSelect(prefix + "mailingCountry", this.buildListSelection("mailingCountryId"), "mailingCountryId"));
    formItems.push(new UiFormSelect(prefix + "mailingState", this.buildListSelection("mailingStateId"), "mailingStateId"));
    formItems.push(new UiFormSelect(prefix + "mailingCity", this.buildListSelection("mailingCityId"), "mailingCityId"));
    formItems.push(new UiFormInput(prefix + "mailingDistrict", "mailingDistrict"));
    formItems.push(new UiFormInput(prefix + "mailingStreetAddressLine1", "mailingStreetAddressLine1"));
    formItems.push(new UiFormInput(prefix + "mailingStreetAddressLine2", "mailingStreetAddressLine2"));
    formItems.push(new UiFormInput(prefix + "mailingZipPostalCode", "mailingZipPostalCode"));
    
    // Data sharing
    formItems.push(new UiFormDivider());
    formItems.push(new UiFormBigHeader(() => that.languageService.formatLanguage(prefix + "dataShare")));
    formItems.push(new UiFormCheckbox(prefix + "consentMarketing", "consentMarketing"));
    formItems.push(new UiFormCheckbox(prefix + "consentNonMarketing", "consentNonMarketing"));
    formItems.push(new UiFormCheckbox(prefix + "consentAbroad", "consentAbroad"));
    formItems.push(new UiFormCheckbox(prefix + "consentTransferToThirdParty", "consentTransferToThirdParty"));
    formItems.push(new UiFormDivider());
    return formItems;
  }

  protected override buildFormInputGroups(): ExpansionPanelInputGroup[] {
    var result: ExpansionPanelInputGroup[] = [];
    let prefix = "individualCustomer.";
    // nationality
    let formItems:UiFormItem[] = [];
    formItems.push(new UiFormComplexInput("nationality", "nationality"));
    result.push(new ExpansionPanelInputGroup(prefix + 'nationality', formItems));
    // nationality
    formItems = [];
    formItems.push(new UiFormComplexInput("secondNationality", "secondNationality"));
    result.push(new ExpansionPanelInputGroup(prefix + 'secondNationality', formItems));
    // Return
    return result;
  }
  
  protected override buildListSelection(selectName: string): UiSelectItem[] {
    if (selectName == 'stateId') {
      return this.filteredStates ? this.filteredStates.map( m => ({
        selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    } else if (selectName == 'mailingStateId'){
      return this.filteredMailingStates ? this.filteredMailingStates.map( m => ({
        selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    } else if (selectName == 'countryId' || selectName == 'mailingCountryId') {
      return this.countries ? this.countries.map( m => ({
        selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    } else if (selectName == 'cityId') {
      return this.filteredCities ? this.filteredCities.map( m => ({
          selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    } else if (selectName == 'mailingCityId') {
      return this.filteredMailingCities ? this.filteredMailingCities.map( m => ({
          selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    }
    return super.buildListSelection(selectName);
  }

  @Input() override set addingItem(item: IndividualCustomer| null) {
    if (item) {
      this.previousCountryId = item.countryId;
      this.previousMailingCountryId = item.mailingCountryId;
    } else {
      this.previousCountryId = null;
      this.previousMailingCountryId = null;
    }
    super.addingItem = item;
  }

  valueChanged(value: any) {
    let selectedCountry = value.countryId;
    let selectedState = value.stateId;
    if (this.previousCountryId != selectedCountry) {
      this.previousCountryId = selectedCountry;
      this.refreshFilteredStates();
      if (this.filteredStates.length > 0) {
        selectedState = this.filteredStates[0].id;
        this.addForm.patchValue({stateId: selectedState})
      }
    } else if (this.filteredStates.length < 1 && this.previousCountryId != null) {
      this.refreshFilteredStates();
    }

    if (this.previousStateId != selectedState) {
      this.previousStateId = selectedState;
      this.refreshFilteredCities(() => {
        if (this.filteredCities.length > 0) {
          this.addForm.patchValue({cityId: (this.filteredCities[0].id as number) })
        }
      });
    } else if (this.filteredCities.length < 1 && this.previousStateId != null) {
      this.refreshFilteredCities(() => {});
    }

    selectedCountry = value.mailingCountryId;
    let selectedMailingState = value.mailingStateId;
    if (this.previousMailingCountryId != selectedCountry) {
      this.previousMailingCountryId = selectedCountry;
      this.refreshfilteredMailingStates();
      if (this.filteredMailingStates.length > 0) {
        this.addForm.patchValue({mailingStateId: (this.filteredMailingStates[0].id as number) })
      }
    } else if (this.filteredMailingStates.length < 1 && this.previousMailingCountryId != null) {
      this.refreshfilteredMailingStates();
    }

    if (this.previousMailingStateId != selectedMailingState) {
      this.previousMailingStateId = selectedMailingState;
      this.refreshFilteredMailingCities(() => {
        if (this.filteredMailingCities.length > 0) {
          this.addForm.patchValue({mailingCityId: (this.filteredCities[0].id as number) })
        }
      });
    } else if (this.filteredMailingCities.length < 1 && this.previousMailingStateId != null) {
      this.refreshFilteredMailingCities(() => {});
    }
  }

  refreshFilteredStates() {
    this.filteredStates = [];
    for (let s of this.states) {
      if (s.countryId == this.previousCountryId) {
        this.filteredStates.push(s);
      }
    }
    this.updateSelectItem("stateId");
  }

  refreshfilteredMailingStates() {
    this.filteredMailingStates = [];
    for (let s of this.states) {
      if (s.countryId == this.previousMailingCountryId) {
        this.filteredMailingStates.push(s);
      }
    }
    this.updateSelectItem("mailingStateId");
  }

  refreshFilteredCities(callback: Function) {
    this.filteredCities = [];
    if (this.previousStateId) {
      this.cityService.filterByStateId(this.previousStateId).subscribe((res: GeneralApiResponse) => {
        if (res.status == 0) {
          for (let c of (res.result as City[])) {
            this.filteredCities.push(c);
          }
          this.updateSelectItem("cityId");
          callback();
        }
      });
    } else {
      this.updateSelectItem("cityId");
    }
  }
  
  refreshFilteredMailingCities(callback: Function) {
    this.filteredMailingCities = [];
    if (this.previousMailingStateId) {
      this.cityService.filterByStateId(this.previousMailingStateId).subscribe((res: GeneralApiResponse) => {
        if (res.status == 0) {
          for (let c of (res.result as City[])) {
            this.filteredMailingCities.push(c);
          }
          this.updateSelectItem("mailingCityId");
          callback();
        }
      });
    } else {
      this.updateSelectItem("mailingCityId");
    }
  }

  protected override get additionalFormGroupElement(): any {
    return {
      nationality: this.formBuilder.group(new Nationality()),
      secondNationality: this.formBuilder.group(new Nationality())
    };
  }
}
