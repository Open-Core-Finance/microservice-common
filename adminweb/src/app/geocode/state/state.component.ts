import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { AppComponent } from 'src/app/app.component';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';
import { environment } from 'src/environments/environment';
import { AddStateComponent } from '../add-state/add-state.component';
import { State } from 'src/app/classes/geocode/State';
import { RestService } from 'src/app/services/rest.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { LanguageService } from 'src/app/services/language.service';
import { CommonService } from 'src/app/services/common.service';
import { MatDialog } from '@angular/material/dialog';
import { OrganizationService } from 'src/app/services/organization.service';
import { PermissionService } from 'src/app/services/permission.service';
import { Subscription } from 'rxjs';
import { Country } from 'src/app/classes/geocode/Country';
import { CountryService } from 'src/app/services/geocode.service';

@Component({
  selector: 'app-state',
  standalone: true,
  imports: [CommonModule, SharedModule, AddStateComponent],
  templateUrl: './state.component.html',
  styleUrl: './state.component.sass'
})
export class StateComponent extends TableComponent<State> {

  private countries: Country[] = [];
  private countriesSubscription: Subscription | undefined;

  public constructor(protected override restService: RestService, public override auth: AuthenticationService,
    protected override http: HttpClient, protected override router: Router, public override languageService: LanguageService,
    protected override commonService: CommonService, public override dialog: MatDialog,
    protected override organizationService: OrganizationService,
    protected override permissionService: PermissionService, protected override changeDetector: ChangeDetectorRef,
    private countryService: CountryService) {
      super(restService, auth, http, router, languageService, commonService, dialog, organizationService, permissionService,
        changeDetector);
      this.countriesSubscription?.unsubscribe();
      this.countriesSubscription = this.countryService.entityListSubject.subscribe( items => this.countries = items);
  }

  override ngOnDestroy(): void {
    super.ngOnDestroy();
    this.countriesSubscription?.unsubscribe();
  }

  override permissionResourceName(): string {
    return "state";
  }

  override get localizePrefix(): string {
    return "states";
  }

  parent: AppComponent | undefined;

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("countryCode", labelKeyPrefix + "countryCode"));
    result.push(new TableColumnUi("countryId", labelKeyPrefix + "country", {complex: true}));
    result.push(new TableColumnUi("enabled", labelKeyPrefix + "enabled"));
    result.push(new TableColumnUi("wikiDataId", labelKeyPrefix + "wikiDataId"));
    result.push(new TableColumnUi("lastModifiedDate", labelKeyPrefix + "lastModifiedDate", {
      dateFormat: "yyyy-MM-dd hh:mm:ss"
    }));
    result.push(new TableColumnUi("fipsCode", labelKeyPrefix + "fipsCode"));
    result.push(new TableColumnUi("type", labelKeyPrefix + "type"));
    result.push(new TableColumnUi("iso2", labelKeyPrefix + "iso2"));
    result.push(new TableColumnUi("latitude", labelKeyPrefix + "latitude", {numberFormat: '1.0-2'}));
    result.push(new TableColumnUi("longitude", labelKeyPrefix + "longitude", {numberFormat: '1.0-2'}));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.state;
  }

  override createNewItem(): State {
    return new State();
  }

  override get indexColumnLabelKey(): string {
    return "";
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