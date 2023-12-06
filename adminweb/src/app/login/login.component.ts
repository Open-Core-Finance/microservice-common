import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { LanguageService } from '../services/language.service';
import { LanguageItem } from '../classes/LanguageItem';
import { AppSettings } from '../classes/AppSetting';
import { AuthenticationService } from '../services/authentication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {
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

  constructor(public languageService: LanguageService, private auth: AuthenticationService, private router: Router,
    private route: ActivatedRoute) {
    const that = this;
    languageService.languageDataObservable.subscribe(languageData => that.refreshLanguage(languageData));
    languageService.languageListObservable.subscribe( list => this.languageList = list);
    this.changeLanguage(languageService.selectedLanguage);
    that.auth.currentSession.subscribe(x => {
      if (x != null) {
        let urlToNavigate = that.returnUrl || "/" + environment.frontEndUrl.organizations;
        that.router.navigate([urlToNavigate]);
      }
    });
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'];
  }

  submit() {
    this.clearMessages();
    this.auth.login(this.loginForm.value, (data: any) => {
        if (data.status === 0) {
            // const loginSession = data.result as LoginSession;
            if (this.returnUrl) {
              this.router.navigate([this.returnUrl]);
            }
        } else {
            this.clearMessages();
            var err = data.error;
            if (err.statusCode) {
                this.message['error'].push(err.statusCode);
            } else if (err.message != null) {
                this.message['error'].push(err.message);
            } else {
                this.message['error'].push(data);
            }
        }
    });
    // if (this.router.url.indexOf(this.authGuard.noPerError) > -1) {
    //     this.clearMessages();
    //     this.message.error.push(this.languageData.permissionDenied);
    // }
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
}
