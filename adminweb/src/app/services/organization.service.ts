import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { Organization } from '../classes/Organization';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Observable } from 'rxjs/internal/Observable';
import { Subscription } from 'rxjs';
import { AppSettings } from '../classes/AppSetting';

@Injectable({
  providedIn: 'root'
})
export class OrganizationService implements OnInit, OnDestroy {

  private organizationSubject: BehaviorSubject<Organization | null>;
  public organizationObservable: Observable<Organization | null>;

  constructor() {
    const savedOrg = this.organization;
    this.organizationSubject = new BehaviorSubject<Organization | null>(savedOrg);
    this.organizationObservable = this.organizationSubject.asObservable();
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  public get organization(): Organization | null {
    const savedOrg = sessionStorage.getItem(AppSettings.LOCAL_KEY_SAVED_ORGANIZATION);
    if (savedOrg != null) {
      return JSON.parse(savedOrg) as Organization;
    }
    return null;
  }

  public set organization(organization: Organization | null) {
    sessionStorage.setItem(AppSettings.LOCAL_KEY_SAVED_ORGANIZATION, JSON.stringify(organization));
    this.organizationSubject.next(organization);
  }

}
