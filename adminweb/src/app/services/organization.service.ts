import { Injectable } from '@angular/core';
import { Organization } from '../classes/Organization';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {

  public organizationSubject: BehaviorSubject<Organization | null>;
  public organizationObservable: Observable<Organization | null>;
  public organization: Organization | null = null;

  constructor() {
    this.organizationSubject = new BehaviorSubject<Organization | null>(null);
    this.organizationObservable = this.organizationSubject.asObservable();
    this.organizationObservable.subscribe(s => this.organization = s);
  }
}
