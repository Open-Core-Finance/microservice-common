import {Component, ViewChild, OnDestroy, OnInit, AfterViewInit} from '@angular/core';
import { LanguageMenuItem, MenuGroup, MenuItem } from './classes/Menu';
import { environment } from 'src/environments/environment';
import { LanguageKeyLabelProvider, StaticTextLabelProvider } from './classes/LabelProvider';
import { LanguageService } from './services/language.service';
import { LoginSession } from './classes/LoginSession';
import { AuthenticationService } from './services/authentication.service';
import { MatDrawer } from '@angular/material/sidenav';
import { Subscription } from 'rxjs';
import { LanguageItem } from './classes/LanguageItem';
import { Role } from './classes/Role';
import {LeftMenuComponent} from "./left-menu/left-menu.component";
import {RouterOutlet} from "@angular/router";
import { Organization } from './classes/Organization';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.sass'],
    standalone: false
})
export class AppComponent implements OnDestroy, OnInit, AfterViewInit {
  title = 'corefinance-adminweb';
  commonLoginSession: LoginSession | null = null;
  menuGroups: MenuGroup[] = [];
  @ViewChild('drawer') drawer: MatDrawer | undefined;
  @ViewChild('leftMenu') leftMenu: LeftMenuComponent | undefined;
  @ViewChild('routerOutlet') routerOutlet: RouterOutlet | undefined;

  selectedRole: Role | null | undefined;

  languageSubscription: Subscription | null = null;
  sessionSubscription: Subscription | null = null;
  selectedRoleSubscription: Subscription | null = null;

  constructor(private languageService: LanguageService,
     private authenticationService: AuthenticationService) {
    this.commonLoginSession = authenticationService.currentSessionValue;
    this.selectedRole = null;
  }

  ngOnInit(): void {
    this.sessionSubscription?.unsubscribe();
    this.sessionSubscription = this.authenticationService.currentSessionSubject.subscribe(session => this.commonLoginSession = session);
    this.selectedRoleSubscription?.unsubscribe();
    this.selectedRoleSubscription = this.authenticationService.selectedRoleSubject.subscribe(r => this.selectedRole = r);
    this.rebuildMenuItems();
  }

  ngOnDestroy(): void {
    this.sessionSubscription?.unsubscribe();
    this.languageSubscription?.unsubscribe();
    this.selectedRoleSubscription?.unsubscribe();
  }

  rebuildMenuItems() {
    // Main Group
    this.menuGroups = [ this.masterMenu, this.productMenu, this.customerMenu, this.accountMenu, this.transactionMenu,
      this.uamMenu, this.languageMenu, this.geocodeMenu ];
    // Generate menu group ID
    for (let  i = 0; i < this.menuGroups.length; i++) {
      const menuGroup = this.menuGroups[i];
      let index = i + 1;
      let menuId;
      if (index < 10) {
        menuId = "0" + index;
      } else {
        menuId = "" + index;
      }
      menuGroup.id = menuId;
    }
  }

  public toggleMenuClicked() {
    this.drawer?.toggle();
  }

  public openMenu() {
    this.drawer?.open();
  }

  public closeMenu() {
    this.drawer?.close();
  }

  languageClick(langiageItem: LanguageItem) {
    this.languageService.changeLanguage(langiageItem);
  }

  private isVisibleOrganizationMenu(selectedRole: Role | null, organization: Organization | null): boolean {
    if (selectedRole != undefined && selectedRole != null) {
      return selectedRole.tenantId == null || selectedRole.tenantId?.trim()?.length < 1;
    }
    return false;
  }

  private isVisibleOrganizationDetailsMenu(selectedRole: Role | null, organization: Organization | null): boolean {
    if (organization != undefined && organization != null) {
      return true;
    }
    return false;
  }

  private get masterMenu(): MenuGroup {
    const languageService = this.languageService;
    const organizationMenuItem = new MenuItem(environment.frontEndUrl.organizations,
      new LanguageKeyLabelProvider(languageService, "menu.organizations", []), "assets/images/organization-icon.svg", "", this.isVisibleOrganizationMenu);
    const organizationDetailsMenuItem = new MenuItem(environment.frontEndUrl.organizationDetails,
        new LanguageKeyLabelProvider(languageService, "menu.organizationDetails", []), "assets/images/organization-icon.svg", "", this.isVisibleOrganizationDetailsMenu);
    const currenciesMenuItem = new MenuItem(environment.frontEndUrl.currencies, new LanguageKeyLabelProvider(languageService, "menu.currencies", []), "", "security", null);
    const holidaysMenuItem = new MenuItem(environment.frontEndUrl.holidays, new LanguageKeyLabelProvider(languageService, "menu.holidays", []), "",
      "calendar_month", this.isVisibleOrganizationDetailsMenu);
    const branchesMenuItem = new MenuItem(environment.frontEndUrl.branches, new LanguageKeyLabelProvider(languageService, "menu.branches", []), "",
      "location_city", this.isVisibleOrganizationDetailsMenu);
    return new MenuGroup("", new LanguageKeyLabelProvider(languageService, "menu.groupMaster", []),
      [organizationMenuItem, organizationDetailsMenuItem, currenciesMenuItem, holidaysMenuItem, branchesMenuItem], null);
  }

  private get productMenu(): MenuGroup {
    const languageService = this.languageService;
    const depositProductMenuItem = new MenuItem(environment.frontEndUrl.depositProducts, new LanguageKeyLabelProvider(languageService, "menu.depositProduct", []), "", "account_balance", null);
    const productCategoryMenuItem = new MenuItem(environment.frontEndUrl.productCategories, new LanguageKeyLabelProvider(languageService, "menu.productCategory", []), "", "category", null);
    const productTypeMenuItem = new MenuItem(environment.frontEndUrl.productTypes, new LanguageKeyLabelProvider(languageService, "menu.productType", []), "", "type_specimen", null);
    const loanProductMenuItem = new MenuItem(environment.frontEndUrl.loanProducts, new LanguageKeyLabelProvider(languageService, "menu.loanProduct", []), "", "real_estate_agent", null);
    const glProductMenuItem = new MenuItem(environment.frontEndUrl.glProducts, new LanguageKeyLabelProvider(languageService, "menu.glProduct", []), "", "account_balance_wallet", null);
    const cryptoProductMenuItem = new MenuItem(environment.frontEndUrl.cryptoProducts, new LanguageKeyLabelProvider(languageService, "menu.cryptoProduct", []), "", "currency_bitcoin", null);
    const exchangeRatesMenuItem = new MenuItem(environment.frontEndUrl.exchangeRates, new LanguageKeyLabelProvider(languageService, "menu.exchangeRate", []), "", "currency_exchange", null);
    const ratesMenuItem = new MenuItem(environment.frontEndUrl.rates, new LanguageKeyLabelProvider(languageService, "menu.rate", []), "", "paid", null);
    const rateSourceMenuItem = new MenuItem(environment.frontEndUrl.rateSources, new LanguageKeyLabelProvider(languageService, "menu.rateSource", []), "", "receipt", null);
    return new MenuGroup("", new LanguageKeyLabelProvider(languageService, "menu.groupProduct", []),
      [productCategoryMenuItem, productTypeMenuItem, depositProductMenuItem, loanProductMenuItem, glProductMenuItem, cryptoProductMenuItem, exchangeRatesMenuItem, ratesMenuItem, rateSourceMenuItem], this.isVisibleOrganizationDetailsMenu);
  }

  private get languageMenu(): MenuGroup {
    const that = this;
    const languageMenu = new  MenuGroup("", new StaticTextLabelProvider("Language"), [], null);
    this.languageSubscription?.unsubscribe();
    this.languageSubscription = this.languageService.languageListSubject.subscribe( langs => {
      languageMenu.items = [];
      for (var item of langs) {
        const langMenuItem = new LanguageMenuItem(new StaticTextLabelProvider(item.name), item.languageKey, item, null)
        languageMenu.items.push(langMenuItem);
        langMenuItem.action = function(item: LanguageMenuItem) {
          that.languageClick(item.languageItem);
        }
      }
    });
    return languageMenu;
  }

  private get accountMenu(): MenuGroup {
    const languageService = this.languageService;
    const depositMenuItem = new MenuItem(environment.frontEndUrl.depositAccounts, new LanguageKeyLabelProvider(languageService, "menu.depositAccount", []), "", "account_balance", null);
    const loanMenuItem = new MenuItem(environment.frontEndUrl.loanAccounts, new LanguageKeyLabelProvider(languageService, "menu.loanAccount", []), "", "real_estate_agent", null);
    const glMenuItem = new MenuItem(environment.frontEndUrl.glAccounts, new LanguageKeyLabelProvider(languageService, "menu.glAccount", []), "", "account_balance_wallet", null);
    const cryptoMenuItem = new MenuItem(environment.frontEndUrl.cryptoAccounts, new LanguageKeyLabelProvider(languageService, "menu.cryptoAccount", []), "", "currency_bitcoin", null);
    return new MenuGroup("", new LanguageKeyLabelProvider(languageService, "menu.groupAccount", []), [
      depositMenuItem, loanMenuItem, glMenuItem, cryptoMenuItem
    ], this.isVisibleOrganizationDetailsMenu);
  }

  private get uamMenu(): MenuGroup {
    const languageService = this.languageService;
    const userManagementItem = new MenuItem(environment.frontEndUrl.userManagement, new LanguageKeyLabelProvider(languageService, "menu.user", []), "", "person", null);
    const roleManagementItem = new MenuItem(environment.frontEndUrl.roleManagement,
        new LanguageKeyLabelProvider(languageService, "menu.role", []), "", "user_attributes", null);
    return new MenuGroup("", new LanguageKeyLabelProvider(languageService, "menu.groupUam", []),
      [userManagementItem, roleManagementItem], null);
  }

  private get customerMenu(): MenuGroup {
    const languageService = this.languageService;
    const individualCustomerItem = new MenuItem(environment.frontEndUrl.individualCustomer, new LanguageKeyLabelProvider(languageService, "menu.individualCustomer", []), "", "person", null);
    const corporateCustomerItem = new MenuItem(environment.frontEndUrl.corporateCustomer, new LanguageKeyLabelProvider(languageService, "menu.corporateCustomer", []), "", "corporate_fare", null);
    return new MenuGroup("", new LanguageKeyLabelProvider(languageService, "menu.groupCustomer", []),
      [individualCustomerItem, corporateCustomerItem], this.isVisibleOrganizationDetailsMenu);
  }

  private get geocodeMenu(): MenuGroup {
    const languageService = this.languageService;
    const regionItem = new MenuItem(environment.frontEndUrl.region, new LanguageKeyLabelProvider(languageService, "menu.region", []), "", "public", null);
    const subregionItem = new MenuItem(environment.frontEndUrl.subRegion, new LanguageKeyLabelProvider(languageService, "menu.subregion", []), "", "south_america", null);
    const countryItem = new MenuItem(environment.frontEndUrl.country, new LanguageKeyLabelProvider(languageService, "menu.country", []), "", "map", null);
    const stateItem = new MenuItem(environment.frontEndUrl.state, new LanguageKeyLabelProvider(languageService, "menu.state", []), "", "home_pin", null);
    const cityItem = new MenuItem(environment.frontEndUrl.city, new LanguageKeyLabelProvider(languageService, "menu.city", []), "", "home_pin", null);
    return new MenuGroup("", new LanguageKeyLabelProvider(languageService, "menu.groupGeocode", []),
      [regionItem, subregionItem, countryItem, stateItem, cityItem], null);
  }

  private get transactionMenu(): MenuGroup {
    const languageService = this.languageService;
    const fundTransferMenuItem = new MenuItem(environment.frontEndUrl.fundTransfer, new LanguageKeyLabelProvider(languageService, "menu.fundTransfer", []), "", "move_down", null);
    return new MenuGroup("", new LanguageKeyLabelProvider(languageService, "menu.groupTransactions", []), [
      fundTransferMenuItem
    ], this.isVisibleOrganizationDetailsMenu);
  }

  ngAfterViewInit(): void {
  }

  onActive($event: any) {
    $event.parent = this;
  }
}
