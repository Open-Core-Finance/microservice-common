import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { LanguageService } from '../services/language.service';
import { LanguageItem } from '../classes/LanguageItem';
import { AppSettings } from '../classes/AppSetting';
import { AuthenticationService } from '../services/authentication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { Role } from '../classes/Role';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });
  selectedLanguage = AppSettings.LANGUAGE_DEFAULT;
  languageData: Record<string, any> = {};
  languageList: LanguageItem[] = [];
  message: Record<string, any[]> = {
    success: [],
    error: []
  };
  returnUrl: string | null = null;
  isLoading = false;
  roleList: Role[] = [];

  languageSubscription: Subscription | null = null;
  languageListSubscription: Subscription | null = null;
  loginErrorSubscription: Subscription | null = null;
  roleListSubscription: Subscription | null = null;
  selectedRoleSubscription: Subscription | null = null;

  constructor(public languageService: LanguageService, private auth: AuthenticationService, private router: Router,
    private route: ActivatedRoute) {
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
    this.languageSubscription = this.languageService.languageDataObservable.subscribe(languageData => that.refreshLanguage(languageData));
    this.languageListSubscription = this.languageService.languageListObservable.subscribe( list => this.languageList = list);
    this.changeLanguage(this.languageService.selectedLanguage);
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'];
    this.loginErrorSubscription = this.auth.loginErrorObservable.subscribe(err => {
      if (err != null && err.length > 0) {
        this.isLoading = false;
        this.clearMessages();
        this.message['error'].push(err);
      }
    });
    this.roleListSubscription = this.auth.roleListObservable.subscribe(list => {
      if (list.length > 0) {
        this.isLoading = false;
        this.clearMessages();
      }
      this.roleList = list;
      if (this.roleList.length === 1) {
        this.auth.selectedRoleSubject.next(this.roleList[0]);
      }
    });
    this.selectedRoleSubscription = this.auth.selectedRoleObservable.subscribe( role => {
      if (role != null) {
        let urlToNavigate: string;
        if (role.organization == null) {
          urlToNavigate = that.returnUrl || "/" + environment.frontEndUrl.organizations;
        } else {
          urlToNavigate = that.returnUrl || "/" + environment.frontEndUrl.organizationDetails;
        }
        that.router.navigate([urlToNavigate]);
      }
    });
  }

  submit() {
    this.clearMessages();
    this.isLoading = true;
    this.auth.login(this.loginForm.value, (data: any) => {});
  }

  refreshLanguage(languageData: Record<string, any>) {
    this.languageData = languageData;
  }

  changeLanguage(languageItem: LanguageItem) {
    this.languageService.changeLanguage(languageItem);
    this.selectedLanguage = languageItem;
  }

  clearMessages() {
    this.message = {
        success: [],
        error: []
    };
  }

  logout($event: any): any {
    this.auth.logout();
    window.location.reload();
  }

  selectRole(roleId: string) {
    for (let role of this.roleList) {
      if (role.id == roleId) {
        this.auth.selectedRoleSubject.next(role);
        break;
      }
    }
  }
}
