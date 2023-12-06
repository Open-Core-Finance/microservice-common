import { Component, ViewChild, Input, Output, OnDestroy } from '@angular/core';
import { LanguageMenuItem, MenuGroup, MenuItem } from './classes/Menu';
import { OrganizationService } from './services/organization.service';
import { environment } from 'src/environments/environment';
import { LanguageKeyLabelProvider, OrganizationNameLabelProvider, StaticTextLabelProvider } from './classes/LabelProvider';
import { LanguageService } from './services/language.service';
import { LoginSession } from './classes/LoginSession';
import { AuthenticationService } from './services/authentication.service';
import { MatDrawer } from '@angular/material/sidenav';
import { Subscription } from 'rxjs';
import { LanguageItem } from './classes/LanguageItem';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements OnDestroy {
  title = 'corefinance-adminweb';
  loginSession: LoginSession | null = null;
  menuGroups: MenuGroup[] = [];
  @ViewChild('drawer') drawer: MatDrawer | undefined;
  languageSubscription: Subscription | undefined;

  constructor(private organizationService: OrganizationService, private languageService: LanguageService,
     authenticationService: AuthenticationService) {
    authenticationService.currentSession.subscribe(session => this.loginSession = session);
    this.loginSession = authenticationService.currentSessionValue;
    this.rebuildMenuItems();
  }

  ngOnDestroy(): void {
    if (this.languageSubscription) {
      this.languageSubscription.unsubscribe();
    }
  }

  rebuildMenuItems() {
    const languageService = this.languageService;
    // System menu group
    const schoolMenuItem = new MenuItem(environment.frontEndUrl.organizations, new LanguageKeyLabelProvider(languageService, "menu.organizations", []), "assets/images/organization-icon.svg", "");
    const curenciesMenuItem = new MenuItem(environment.frontEndUrl.curencies, new LanguageKeyLabelProvider(languageService, "menu.curencies", []), "", "security");
    const systemGroup = new MenuGroup("01", new LanguageKeyLabelProvider(languageService, "menu.groupSystem", []), [schoolMenuItem, curenciesMenuItem]);
    // Organization menu group
    // const userMenuItem = new MenuItem(environment.frontEndUrl.users, new LanguageKeyLabelProvider(languageService, "menu.users", []), "assets/images/user-icon.svg", "");
    // userMenuItem.activated = false;
    // const subjectMenuItem = new MenuItem(environment.frontEndUrl.subjects, new LanguageKeyLabelProvider(languageService, "menu.subjects", []), "", "medical_information");
    // const periodMenuItem = new MenuItem(environment.frontEndUrl.periods, new LanguageKeyLabelProvider(languageService, "menu.periods", []), "", "view_timeline");
    // const courseMenuItem = new MenuItem(environment.frontEndUrl.courses, new LanguageKeyLabelProvider(languageService, "menu.courses", []), "", "menu_book");
    // const feedMenuItem = new MenuItem(environment.frontEndUrl.feeds, new LanguageKeyLabelProvider(languageService, "menu.feeds", []), "", "feed");
    // feedMenuItem.activated = false;
    // const classMenuItem = new MenuItem(environment.frontEndUrl.classes, new LanguageKeyLabelProvider(languageService, "menu.classes", []), "", "other_houses");
    // const registrationMenuItem = new MenuItem(environment.frontEndUrl.registrations, new LanguageKeyLabelProvider(languageService, "menu.registrations", []), "", "calendar_month");
    // const schoolMenuGroup = new MenuGroup("01", new OrganizationNameLabelProvider(this.organizationService),
    //     [classMenuItem, subjectMenuItem, periodMenuItem, courseMenuItem, registrationMenuItem, userMenuItem, feedMenuItem]);
    // Other menu group
    const settingMenuItem = new MenuItem(environment.frontEndUrl.settings, new LanguageKeyLabelProvider(languageService, "menu.settings", []), "", "settings");
    const otherGroup = new MenuGroup("03", new LanguageKeyLabelProvider(languageService, "menu.groupOther", []), [settingMenuItem]);
    settingMenuItem.activated = false;
    // Language
    var that = this;
    const languageMenu = new  MenuGroup("04", new StaticTextLabelProvider("Language"), []);
    if (this.languageSubscription) {
      this.languageSubscription.unsubscribe();
    }
    this.languageSubscription = this.languageService.languageListObservable.subscribe( langs => {
      languageMenu.items = [];
      for (var item of langs) {
        const langMenuItem = new LanguageMenuItem(new StaticTextLabelProvider(item.name), item.languageKey, item)
        languageMenu.items.push(langMenuItem);
        langMenuItem.action = function(item: LanguageMenuItem) {
          that.languageClick(item.languageItem);
        }
      }
    });
    // Main Group
    this.menuGroups = [ systemGroup, otherGroup, languageMenu ];
  }

  toggleMenuClicked() {
    this.drawer?.toggle();
  }

  languageClick(langiageItem: LanguageItem) {
    this.languageService.changeLanguage(langiageItem);
  }
}
