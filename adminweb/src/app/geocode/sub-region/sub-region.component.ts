import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Region } from 'src/app/classes/geocode/Region';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { TableComponent } from 'src/app/generic-component/TableComponent';
import { AppComponent } from 'src/app/app.component';
import { TableColumnUi } from 'src/app/classes/ui/UiTableDisplay';
import { environment } from 'src/environments/environment';
import { SubRegion } from 'src/app/classes/geocode/SubRegion';
import { RestService } from 'src/app/services/rest.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { LanguageService } from 'src/app/services/language.service';
import { CommonService } from 'src/app/services/common.service';
import { MatDialog } from '@angular/material/dialog';
import { OrganizationService } from 'src/app/services/organization.service';
import { PermissionService } from 'src/app/services/permission.service';
import { RegionService } from 'src/app/services/geocode.service';
import { Subscription } from 'rxjs';
import { AddSubRegionComponent } from '../add-sub-region/add-sub-region.component';

@Component({
    selector: 'app-sub-region',
    imports: [CommonModule, SharedModule, AddSubRegionComponent],
    templateUrl: './sub-region.component.html',
    styleUrl: './sub-region.component.sass'
})
export class SubRegionComponent extends TableComponent<SubRegion> {

  private regions: Region[] = [];
  private regionsSubscription: Subscription | undefined;

  public constructor(protected override restService: RestService, public override auth: AuthenticationService,
    protected override http: HttpClient, protected override router: Router, public override languageService: LanguageService,
    protected override commonService: CommonService, public override dialog: MatDialog,
    protected override organizationService: OrganizationService,
    protected override permissionService: PermissionService, protected override changeDetector: ChangeDetectorRef,
    private regionService: RegionService) {
      super(restService, auth, http, router, languageService, commonService, dialog, organizationService, permissionService,
        changeDetector);
      this.regionsSubscription?.unsubscribe();
      this.regionsSubscription = this.regionService.entityListSubject.subscribe( items => this.regions = items);
  }

  override ngOnDestroy(): void {
    super.ngOnDestroy();
    this.regionsSubscription?.unsubscribe();
  }

  override permissionResourceName(): string {
    return "subRegion";
  }

  parent: AppComponent | undefined;

  override get tableUiColumns(): TableColumnUi[] {
    const labelKeyPrefix = this.localizePrefix + ".";
    var result: TableColumnUi[] = [];
    result.push(new TableColumnUi("id", labelKeyPrefix + "id"));
    result.push(new TableColumnUi("name", labelKeyPrefix + "name"));
    result.push(new TableColumnUi("regionId", labelKeyPrefix + "region", {
      complex: true
    }));
    result.push(new TableColumnUi("enabled", labelKeyPrefix + "enabled"));
    result.push(new TableColumnUi("wikiDataId", labelKeyPrefix + "wikiDataId"));
    result.push(new TableColumnUi("lastModifiedDate", labelKeyPrefix + "lastModifiedDate", {
      dateFormat: "yyyy-MM-dd hh:mm:ss"
    }));
    result.push(new TableColumnUi("translations", labelKeyPrefix + "translations"));
    return result;
  }

  getServiceUrl() {
    return environment.apiUrl.subRegion;
  }

  override createNewItem(): SubRegion {
    return new SubRegion();
  }

  override get indexColumnLabelKey(): string {
    return "";
  }

  getRegionName(regionId: number) {
    let result = regionId + "";
    if (this.regions) {
      for (let r of this.regions) {
        if (r.id == regionId) {
          return r.name;
        }
      }
    }
    return result;
  }
}