import { Component, Input, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { ActionMenuItem, LanguageMenuItem, MenuGroup, MenuItem } from '../classes/Menu';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { OrganizationService } from '../services/organization.service';
import { Role } from '../classes/Role';
import { Subscription } from 'rxjs';
import { Organization } from '../classes/Organization';

@Component({
  selector: 'app-left-menu',
  templateUrl: './left-menu.component.html',
  styleUrls: ['./left-menu.component.sass']
})
export class LeftMenuComponent implements OnInit, OnDestroy {

  @Input()
  menuGroups: MenuGroup[] = [];
  @Output() menuClosedEmitter = new EventEmitter();

  private selectedRole: Role | null = null;
  private selectedRoleSubscription: Subscription | undefined;
  private organization: Organization | null = null;
  private organizationSubscription: Subscription | undefined;

  constructor(private router: Router, private route: ActivatedRoute, private auth: AuthenticationService,
    private organizationService: OrganizationService) {
  }

  ngOnInit(): void {
    this.selectedRoleSubscription?.unsubscribe();
    this.selectedRoleSubscription = this.auth.selectedRoleSubject.subscribe( role => this.selectedRole = role);
    this.organizationSubscription?.unsubscribe();
    this.organizationSubscription = this.organizationService.organizationSubject.subscribe(org => this.organization = org);
  }

  menuItemClicked(menuItem: MenuItem) {
    if (!menuItem.activated) {
      return;
    }
    const urlToNavigate = "/" + menuItem.activateUrl;
    const sameUrl = this.isSameUrl(menuItem);
    if (!sameUrl) {
      this.router.navigateByUrl(urlToNavigate);
      //this.toggleMenu();
    }
  }

  isSameUrl(menuItem: MenuItem) {
    if (this.isActionItem(menuItem)) {
      return false;
    }
    const currentUrl = this.router.url;
    const urlToNavigate = "/" + menuItem.activateUrl;
    return currentUrl === urlToNavigate;
  }

  toggleMenu() {
    if (this.menuClosedEmitter) {
      this.menuClosedEmitter.emit();
    }
  }

  ngOnDestroy() {
    this.selectedRoleSubscription?.unsubscribe();
    this.organizationSubscription?.unsubscribe();
  }

  isActionItem(menuItem: MenuItem): boolean {
    return (menuItem instanceof ActionMenuItem);
  }

  isLanguageItem(menuItem: MenuItem): boolean {
    return (menuItem instanceof LanguageMenuItem);
  }

  callMenuAction(menuItem: MenuItem) {
    if (menuItem instanceof ActionMenuItem) {
      if (menuItem.action) {
        menuItem.action(menuItem);
      }
      // this.toggleMenu();
    }
  }

  isVisibleMenu(visibleFn: Function | null) {
    var result = true;
    if (visibleFn != null) {
      result = visibleFn(this.selectedRole, this.organization);
    }
    return result;
  }
}
