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
import { Region } from 'src/app/classes/geocode/Region';
import { Subscription } from 'rxjs';
import { RegionService, SubRegionService } from 'src/app/services/geocode.service';
import { Country } from 'src/app/classes/geocode/Country';
import { SubRegion } from 'src/app/classes/geocode/SubRegion';

@Component({
    selector: 'app-add-country',
    imports: [CommonModule, SharedModule],
    templateUrl: './add-country.component.html',
    styleUrl: './add-country.component.sass'
})
export class AddCountryComponent extends GeneralEntityAddComponent<Country> implements OnDestroy, OnInit {

  private regions: Region[] = [];
  private regionsSubscription: Subscription | undefined;
  private subRegions: SubRegion[] = [];
  private subRegionsSubscription: Subscription | undefined;
  private filteredSubRegions: SubRegion[] = [];
  private valueChangeSubscription: Subscription | undefined;
  private previousRegionId: number | null = null;

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService, private regionService: RegionService,
    private subRegionService: SubRegionService) {
      super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
  }

  ngOnInit(): void {
    this.regionsSubscription?.unsubscribe();
    this.regionsSubscription = this.regionService.entityListSubject.subscribe( items => {
      this.regions = items;
      this.updateSelectItem("regionId");
    });
    this.subRegionsSubscription?.unsubscribe();
    this.subRegionsSubscription = this.subRegionService.entityListSubject.subscribe( items => {
      this.subRegions = items;
      this.valueChanged(this.addForm.value);
    });
    this.valueChangeSubscription?.unsubscribe();
    this.valueChangeSubscription = this.addForm.valueChanges.subscribe( v => this.valueChanged(v));
  }

  ngOnDestroy(): void {
    this.regionsSubscription?.unsubscribe();
    this.subRegionsSubscription?.unsubscribe();
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
    if (!formData.translations || formData.translations.trim() == '' || formData.translations == '{}') {
      formData.translations = null;
    }
    if (!formData.timezones || formData.timezones.trim() == '' || formData.timezones == '{}') {
      formData.timezones = null;
    }
  }

  protected override newEmptyEntity(): Country {
    return new Country();
  }

  protected override buildFormItems(): UiFormItem[] {
    const formItems: UiFormItem[] = [];
    const prefix = "countries.";
    formItems.push(new UiFormInput(prefix + "name", "name"));
    formItems.push(new UiFormSelect(prefix + "region", this.buildListSelection("regionId"), "regionId"));
    formItems.push(new UiFormSelect(prefix + "subRegion", this.buildListSelection("subRegionId"), "subRegionId"));
    formItems.push(new UiFormCheckbox(prefix + "enabled", "enabled"));
    formItems.push(new UiFormInput(prefix + "wikiDataId", "wikiDataId"));
    formItems.push(new UiFormInput(prefix + "iso2", "iso2"));
    formItems.push(new UiFormInput(prefix + "iso3", "iso3"));
    formItems.push(new UiFormInput(prefix + "numericCode", "numericCode"));
    formItems.push(new UiFormInput(prefix + "phoneCode", "phoneCode"));
    formItems.push(new UiFormInput(prefix + "capital", "capital"));
    formItems.push(new UiFormInput(prefix + "currency", "currency"));
    formItems.push(new UiFormInput(prefix + "currencyName", "currencyName"));
    formItems.push(new UiFormInput(prefix + "currencySymbol", "currencySymbol"));
    formItems.push(new UiFormInput(prefix + "tld", "tld"));
    formItems.push(new UiFormInput(prefix + "nativePeople", "nativePeople"));
    formItems.push(new UiFormInput(prefix + "nationality", "nationality"));
    formItems.push(new UiFormInput(prefix + "latitude", "latitude", "number"));
    formItems.push(new UiFormInput(prefix + "longitude", "longitude", "number"));
    formItems.push(new UiFormInput(prefix + "emoji", "emoji"));
    formItems.push(new UiFormInput(prefix + "emojiu", "emojiu"));
    formItems.push(new UiFormDivider());
    let item: UiFormItem = new UiFormTextarea(prefix + "translations", "translations");
    item.additionClass = "full-width";
    formItems.push(item);
    formItems.push(new UiFormDivider());
    item = new UiFormTextarea(prefix + "timezones", "timezones");
    item.additionClass = "full-width";
    formItems.push(item);
    return formItems;
  }

  protected override buildListSelection(selectName: string): UiSelectItem[] {
    if (selectName == 'regionId') {
      return this.regions ? this.regions.map( m => ({
        selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    } else if (selectName == 'subRegionId') {
      return this.filteredSubRegions ? this.filteredSubRegions.map( m => ({
        selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    }
    return super.buildListSelection(selectName);
  }

  @Input() override set addingItem(item: Country| null) {
    if (item) {
      this.previousRegionId = item.regionId;
    } else {
      this.previousRegionId = null;
    }
    super.addingItem = item;
  }

  valueChanged(value: any) {
    const selectedRegion = value.regionId;
    if (this.previousRegionId != selectedRegion) {
      this.previousRegionId = selectedRegion;
      this.refreshSubRegions();
      if (this.filteredSubRegions.length > 0) {
        this.addForm.patchValue({subRegionId: this.filteredSubRegions[0].id})
      }
    } else if (this.filteredSubRegions.length < 1 && this.previousRegionId != null) {
      this.refreshSubRegions();
    }
  }

  refreshSubRegions() {
    this.filteredSubRegions = [];
    for (let s of this.subRegions) {
      if (s.regionId == this.previousRegionId) {
        this.filteredSubRegions.push(s);
      }
    }
    this.updateSelectItem("subRegionId");
  }
}