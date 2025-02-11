import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { LanguageService } from '../services/language.service';
import { LanguageItem } from '../classes/LanguageItem';
import { AppSettings } from '../classes/AppSetting';
import { AuthenticationService } from '../services/authentication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { Subscription } from 'rxjs';
import { Role } from '../classes/Role';
import { UserMessage } from '../classes/UserMessage';
import { RestService } from '../services/rest.service';
import { OrganizationService } from '../services/organization.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.sass'],
    standalone: false
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });
  selectedLanguage = AppSettings.LANGUAGE_DEFAULT;
  languageData: Record<string, any> = {};
  languageList: LanguageItem[] = [];
  message = new UserMessage([], []);
  returnUrl: string | null = null;
  isLoading = false;
  roleList: Role[] = [];

  languageSubscription: Subscription | null = null;
  languageListSubscription: Subscription | null = null;
  loginErrorSubscription: Subscription | null = null;
  roleListSubscription: Subscription | null = null;
  selectedRoleSubscription: Subscription | null = null;

  constructor(public languageService: LanguageService, private auth: AuthenticationService, private router: Router,
    private route: ActivatedRoute, private restService: RestService, private organizationService: OrganizationService) {
  }

  ngOnDestroy(): void {
    this.languageSubscription?.unsubscribe();
    this.languageListSubscription?.unsubscribe();
    this.roleListSubscription?.unsubscribe();
    this.loginErrorSubscription?.unsubscribe();
    this.selectedRoleSubscription?.unsubscribe();
  }

  ngOnInit(): void {
    const that = this;
    this.languageSubscription = this.languageService.languageDataSubject.subscribe(languageData => that.refreshLanguage(languageData));
    this.languageListSubscription = this.languageService.languageListSubject.subscribe( list => this.languageList = list);
    this.changeLanguage(this.languageService.selectedLanguage);
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'];
    this.roleListSubscription = this.auth.roleListSubject.subscribe(list => {
      if (list.length > 0) {
        this.isLoading = false;
        this.clearMessages();
      }
      this.roleList = list;
      if (this.roleList.length === 1) {
        this.auth.saveSelectedRole(this.roleList[0]);
      }
    });
    this.selectedRoleSubscription = this.auth.selectedRoleSubject.subscribe( (role: Role | null) => {
      if (role != null) {
        let urlToNavigate: string;
        if (role.organization == null) {
          urlToNavigate = that.returnUrl || "/" + environment.frontEndUrl.organizations;
        } else {
          this.organizationService.organizationSubject.next(role.organization);
          urlToNavigate = that.returnUrl || "/" + environment.frontEndUrl.organizationDetails;
        }
        that.router.navigate([urlToNavigate]);
      }
    });
  }

  submit() {
    this.clearMessages();
    this.isLoading = true;
    this.auth.login(this.loginForm.value, this.message, (data: any) => {},
      (data: any) => {
        this.isLoading = false;
        this.restService.handleRestError(data, this.message);
      });
  }

  refreshLanguage(languageData: Record<string, any>) {
    this.languageData = languageData;
  }

  changeLanguage(languageItem: LanguageItem) {
    this.languageService.changeLanguage(languageItem);
    this.selectedLanguage = languageItem;
  }

  clearMessages() {
    this.message.clearAll();
  }

  logout($event: any): any {
    this.auth.logout();
  }

  selectRole(roleId: string) {
    for (let role of this.roleList) {
      if (role.id == roleId) {
        this.auth.saveSelectedRole(role);
        break;
      }
    }
  }
}
