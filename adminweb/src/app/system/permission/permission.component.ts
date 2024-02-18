import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, AfterContentChecked, ChangeDetectorRef } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatCheckboxChange, MatCheckboxModule} from '@angular/material/checkbox';
import {MatListModule} from '@angular/material/list';
import {MatTabsModule} from '@angular/material/tabs';
import {MatIconModule} from '@angular/material/icon';

import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonModule} from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Role } from '../../classes/Role';
import { LanguageService } from '../../services/language.service';
import { PermissionConfig, ResourceActionConfig } from '../../classes/Permission';
import { RestService } from '../../services/rest.service';
import { CommonService } from '../../services/common.service';
import { HttpClient } from '@angular/common/http';
import { PermissionService } from '../../services/permission.service';
import { Subscription } from 'rxjs';
import { environment } from '../../../environments/environment';
import { GeneralApiResponse } from '../../classes/GeneralApiResponse';
import { SharedModule } from 'src/app/generic-component/SharedModule';

@Component({
  selector: 'app-permission',
  standalone: true,
  imports: [CommonModule, MatListModule, FormsModule, ReactiveFormsModule, MatInputModule, MatSelectModule, SharedModule,
    MatButtonModule, MatFormFieldModule, MatCheckboxModule, MatTabsModule, MatIconModule],
  templateUrl: './permission.component.html',
  styleUrl: './permission.component.sass'
})
export class PermissionComponent implements OnInit, OnDestroy, AfterContentChecked {

  @Output() cancel = new EventEmitter();
  @Output() save = new EventEmitter();

  _selectedRole: Role | undefined;
  permissionConfigs: Map<string, PermissionConfig[]> = new Map();
  permissionConfigSubscription: Subscription | undefined;
  permissionErrorSubscription: Subscription | undefined;
  permissionGroups = ["userprofile", "product"];

  constructor(public languageService: LanguageService, private restService: RestService, private commonService: CommonService,
    private http: HttpClient, private permissionService: PermissionService, private changeDetector: ChangeDetectorRef) {
      this.permissionConfigsReloaded(this.permissionService.currentPermissionConfigs);
      this.permissionConfigSubscription?.unsubscribe();
      this.permissionConfigSubscription = this.permissionService.permissionConfigSubject.subscribe(p => {
        this.permissionConfigsReloaded(p);
      });
      this.permissionErrorSubscription?.unsubscribe();
      this.permissionErrorSubscription = this.permissionService.permissionErrorSubject.subscribe(d => this.serviceError(d));
  }

  ngAfterContentChecked(): void {
    this.changeDetector.detectChanges();
  }

  ngOnDestroy(): void {
    this.permissionConfigSubscription?.unsubscribe();
    this.permissionErrorSubscription?.unsubscribe();
  }

  ngOnInit(): void {
  }

  @Input() set selectedRole(role: Role) {
    let needReloadPermissions = false;
    if (role != null) {
      if (this._selectedRole == null) {
        needReloadPermissions = true;
      } else if (this._selectedRole.id != role.id) {
        needReloadPermissions = true;
      }
    }
    this._selectedRole = role;
    if (needReloadPermissions) {
      this.permissionConfigsReloaded(this.permissionConfigs);
    }
  }

  saveClick($event: any) {
    const serviceUrls = this.permissionService.getServiceUrls();
    if (this._selectedRole) {
      const roleId = this._selectedRole.id;
      const totalCount = serviceUrls.length;
      let completedCount = 0;
      for(let serviceUrl of serviceUrls) {
        const permissions = this.permissionService.buildPermissionsFromConfig(
          this.permissionService.filterConfigByServiceUrl(this.permissionConfigs, serviceUrl),
          roleId
        );
        const url = serviceUrl + environment.apiPrefix.permissionOverrdieByRoles + roleId;
        let headers = this.restService.initApplicationJsonRequestHeaders();
        this.http.put<GeneralApiResponse>(url, permissions, {headers}).subscribe({
          next: (data: GeneralApiResponse) => {
            completedCount++;
            if (data.status == 0) {
              if (completedCount >= totalCount) {
                if (this.save) {
                  this.save.emit(data.result);
                }
              }
            } else {
              this.serviceError(data);
            }
          }, error: (data: any) => {
            completedCount++;
            this.serviceError(data);
          }
        });
      }
    } else {
      this.cancelClick($event);
    }
  }

  cancelClick($event: any) {
    if (this.cancel) {
      this.cancel.emit($event);
    }
  }

  serviceError(error: any) {
    if (this.cancel) {
      this.cancel.emit(error);
    }
  }
  
  buildPermissonItemLabel(action: ResourceActionConfig) {
    return this.languageService.formatLanguage(
      "permission.actionItem." + action.action + "_" + action.resourceType + "_" + action.requestMethod,
      []
    );
  }

  checkAllForResourceClicked($event: MatCheckboxChange, permission: PermissionConfig) {
    permission.selected = $event.checked;
    for (let action of permission.actions) {
      action.selected = permission.selected;
    }
  }

  checkActionItemClicked($event: boolean, action: ResourceActionConfig, permission: PermissionConfig) {
    action.selected = $event;
    let checkedAll = true;
    for (let action of permission.actions) {
      if (!action.selected) {
        checkedAll = false;
        break;
      }
    }
    permission.selected = checkedAll;
  }

  permissionConfigsReloaded(p: Map<string, PermissionConfig[]>) {
    this.permissionConfigs = new Map();
    for(let permissionTab of p) {
      var key = permissionTab[0];
      var values = permissionTab[1].flatMap(v => v.clone());
      for(let value of values) {
        value.permissions = [];
      }
      this.permissionConfigs.set(key, values);
    }
    if (this._selectedRole) {
      this.permissionService.loadPermissionByRoles([this._selectedRole.id], p => {
        this.permissionService.mapPermissions(p, this.permissionConfigs);
      });
    }
  }
}
