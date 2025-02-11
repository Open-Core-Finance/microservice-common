import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { FormBuilder } from '@angular/forms';
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
import { Customer } from '../classes/customers/Customer';
import { UiSelectItem } from '../classes/ui/UiFormInput';

@Component({
    imports: [CommonModule, SharedModule],
    template: ''
})
export abstract class GeneralAddCustomerComponent<T extends Customer> extends GeneralEntityAddComponent<T> implements OnDestroy, OnInit {

  protected states: State[] = [];
  protected statesSubscription: Subscription | undefined;
  protected countries: Country[] = [];
  protected countriesSubscription: Subscription | undefined;
  protected valueChangeSubscription: Subscription | undefined;

  protected filteredStates: State[] = [];
  protected previousCountryId: number | null = null;
  protected filteredCities: City[] = [];
  protected previousStateId: number | null = null;

  protected filteredMailingStates: State[] = [];
  protected previousMailingCountryId: number | null = null;
  protected filteredMailingCities: City[] = [];
  protected previousMailingStateId: number | null = null;
  protected previousMailingSameWithAddress: boolean | null = null;

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService,
    protected stateService: StateService, protected countryService: CountryService, protected cityService: CityService) {
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

  @Input() override set addingItem(item: T | null) {
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
          this.addForm.patchValue({mailingCityId: (this.filteredMailingCities[0].id as number) });
        }
      });
    } else if (this.filteredMailingCities.length < 1 && this.previousMailingStateId != null) {
      this.refreshFilteredMailingCities(() => {});
    }

    const mailingSameWithAddress = value.mailingSameWithAddress;
    if (this.previousMailingSameWithAddress != mailingSameWithAddress) {
      this.previousMailingSameWithAddress = mailingSameWithAddress;
      if (value.mailingSameWithAddress) {
        this.addForm.patchValue({
          mailingCountry : value.country,
          mailingCountryId : value.countryId,
          mailingState : value.state,
          mailingStateId : value.stateId,
          mailingCity : value.city,
          mailingCityId : value.cityId,
          mailingDistrict : value.district,
          mailingStreetAddressLine1 : value.streetAddressLine1,
          mailingStreetAddressLine2 : value.streetAddressLine2,
          mailingZipPostalCode : value.zipPostalCode,
        });
        
      }
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

}
