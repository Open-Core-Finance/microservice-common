import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { environment } from 'src/environments/environment';
import { FormBuilder, FormGroup } from '@angular/forms';
import { UiFormDivider, UiFormInput, UiFormItem, UiFormSelect, UiSelectItem } from 'src/app/classes/ui/UiFormInput';
import { UiFormTextarea } from 'src/app/classes/ui/UiFormInput';
import { UiFormCheckbox } from 'src/app/classes/ui/UiFormInput';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { SubRegion } from 'src/app/classes/geocode/SubRegion';
import { LanguageService } from 'src/app/services/language.service';
import { CommonService } from 'src/app/services/common.service';
import { RestService } from 'src/app/services/rest.service';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { Region } from 'src/app/classes/geocode/Region';
import { Subscription } from 'rxjs';
import { RegionService } from 'src/app/services/geocode.service';

@Component({
  selector: 'app-add-sub-region',
  standalone: true,
  imports: [CommonModule, SharedModule],
  templateUrl: './add-sub-region.component.html',
  styleUrl: './add-sub-region.component.sass'
})
export class AddSubRegionComponent extends GeneralEntityAddComponent<SubRegion> implements OnDestroy, OnInit {

  private regions: Region[] = [];
  private regionsSubscription: Subscription | undefined;

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService, private regionService: RegionService) {
      super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
  }

  ngOnInit(): void {
    this.regionsSubscription?.unsubscribe();
    this.regionsSubscription = this.regionService.entityListSubject.subscribe( items => {
      this.regions = items;
      this.updateSelectItem("regionId");
    });
  }

  ngOnDestroy(): void {
    this.regionsSubscription?.unsubscribe();
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.subRegion;
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

  protected override newEmptyEntity(): SubRegion {
    return new SubRegion();
  }

  protected override buildFormItems(): UiFormItem[] {
    const formItems: UiFormItem[] = [];
    const prefix = "subRegion.";
    formItems.push(new UiFormInput(prefix + "name", "name"));
    formItems.push(new UiFormSelect(prefix + "region", this.buildListSelection("regionId"), "regionId"));
    formItems.push(new UiFormCheckbox(prefix + "enabled", "enabled"));
    formItems.push(new UiFormInput(prefix + "wikiDataId", "wikiDataId"));
    formItems.push(new UiFormDivider());
    let item: UiFormItem = new UiFormTextarea(prefix + "translations", "translations");
    item.additionClass = "full-width";
    formItems.push(item);
    return formItems;
  }

  protected override buildListSelection(selectName: string): UiSelectItem[] {
    if (selectName == 'regionId') {
      return this.regions ? this.regions.map( m => ({
        selectValue: m.id, labelKey: m.name
      } as UiSelectItem)) : [];
    }
    return super.buildListSelection(selectName);
  }
}
