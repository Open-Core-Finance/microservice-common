import { AfterViewInit, Component, Input } from '@angular/core';
import { LanguageService } from 'src/app/services/language.service';
import { UserMessage } from 'src/app/classes/UserMessage';

@Component({
  selector: 'app-general-error-panel',
  templateUrl: './general-error-panel.component.html',
  styleUrl: './general-error-panel.component.sass'
})
export class GeneralErrorPanelComponent implements AfterViewInit {

  _message: UserMessage = new UserMessage([], []);
  _errorPrefix: string = "";
  _successPrefix: string = "";
  noInputError: boolean = false;

  constructor(public languageService: LanguageService) {
  }

  ngAfterViewInit(): void {
    if (this._errorPrefix == "") {
      this.noInputError = true;
    } else {
      this.noInputError = false;
    }
  }

  get message(): UserMessage {
    return this._message;
  }

  @Input()
  set message(message: UserMessage) {
    this._message = message;
  }

  get errorPrefix(): string {
    return this._errorPrefix;
  }

  @Input()
  set errorPrefix(errorPrefix: string) {
    this._errorPrefix = errorPrefix;
    if (this._successPrefix == "" && this._errorPrefix != "") {
      this._successPrefix = this._errorPrefix.replace("error", "success");
    }
  }

  get successPrefix(): string {
    return this._successPrefix;
  }

  @Input()
  set successPrefix(successPrefix: string) {
    this._successPrefix = successPrefix;
    if (this._errorPrefix == "" && this._successPrefix != "") {
      this._errorPrefix = this._successPrefix.replace("error", "success");
    }
  }
}
