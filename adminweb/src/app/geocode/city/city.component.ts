import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { City } from 'src/app/classes/geocode/City';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { AppComponent } from 'src/app/app.component';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';
import { environment } from 'src/environments/environment';
import { Country } from 'src/app/classes/geocode/Country';
import { AddCityComponent } from '../add-city/add-city.component';
import { Subscription } from 'rxjs';
import { RestService } from 'src/app/services/rest.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { LanguageService } from 'src/app/services/language.service';
import { CommonService } from 'src/app/services/common.service';
import { MatDialog } from '@angular/material/dialog';
import { OrganizationService } from 'src/app/services/organization.service';
import { PermissionService } from 'src/app/services/permission.service';
import { CountryService, StateService } from 'src/app/services/geocode.service';
import { State } from 'src/app/classes/geocode/State';

@Component({
  selector: 'app-city',
  standalone: true,
  imports: [CommonModule, SharedModule, AddCityComponent],
  templateUrl: './city.component.html',
  styleUrl: './city.component.sass'
})
export class CityComponent extends TableComponent<City> {

  private states: State[] = [];
  private statesSubscription: Subscription | undefined;
  private countries: Country[] = [];
  private countriesSubscription: Subscription | undefined;

  public constructor(protected override restService: RestService, public override auth: AuthenticationService,
    protected override http: HttpClient, protected override router: Router, public override languageService: LanguageService,
    protected override commonService: CommonService, public override dialog: MatDialog,
    protected override organizationService: OrganizationService,
    protected override permissionService: PermissionService, protected override changeDetector: ChangeDetectorRef,
    private stateService: StateService, private countryService: CountryService) {
      super(restService, auth, http, router, languageService, commonService, dialog, organizationService, permissionService,
        changeDetector);
      this.statesSubscription?.unsubscribe();
      this.statesSubscription = this.stateService.entityListSubject.subscribe( items => this.states = items);
      this.countriesSubscription?.unsubscribe();
      this.countriesSubscription = this.countryService.entityListSubject.subscribe( items => this.countries = items);
  }

  override ngOnDestroy(): void {
    super.ngOnDestroy();
    this.statesSubscription?.unsubscribe();
    this.countriesSubscription?.unsubscribe();
  }

  override permissionResourceName(): string {
    return "city";
  }

  override get localizePrefix(): string {
    return "cities";
  }

  parent: AppComponent | undefined;

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("stateCode", labelKeyPrefix + "stateCode"));
    result.push(new TableColumnUi("stateId", labelKeyPrefix + "state", {complex: true}));
    result.push(new TableColumnUi("countryCode", labelKeyPrefix + "countryCode"));
    result.push(new TableColumnUi("countryId", labelKeyPrefix + "country", {complex: true}));
    result.push(new TableColumnUi("enabled", labelKeyPrefix + "enabled"));
    result.push(new TableColumnUi("wikiDataId", labelKeyPrefix + "wikiDataId"));
    result.push(new TableColumnUi("lastModifiedDate", labelKeyPrefix + "lastModifiedDate", {
      dateFormat: "yyyy-MM-dd hh:mm:ss"
    }));
    result.push(new TableColumnUi("latitude", labelKeyPrefix + "latitude", {numberFormat: '1.0-2'}));
    result.push(new TableColumnUi("longitude", labelKeyPrefix + "longitude", {numberFormat: '1.0-2'}));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.city;
  }

  override createNewItem(): City {
    return new City();
  }

  override get indexColumnLabelKey(): string {
    return "";
  }

  getStateName(stateId: number) {
    let result = stateId + "";
    if (this.states) {
      for (let r of this.states) {
        if (r.id == stateId) {
          return r.name;
        }
      }
    }
    return result;
  }

  getCountryName(countryId: number) {
    let result = countryId + "";
    if (this.countries) {
      for (let r of this.countries) {
        if (r.id == countryId) {
          return r.name;
        }
      }
    }
    return result;
  }
}