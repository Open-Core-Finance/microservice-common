import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { LanguageService } from 'src/app/services/language.service';
import { CurrencyService } from 'src/app/services/currency.service';
import { WithdrawalLimit, WithdrawalLimitType } from 'src/app/classes/products/WithdrawalLimit';
import { WithdrawalChannel } from 'src/app/classes/WithdrawalChannel';
import { HttpClient } from '@angular/common/http';
import { GeneralApiResponse } from 'src/app/classes/GeneralApiResponse';
import { environment } from 'src/environments/environment';
import { RestService } from 'src/app/services/rest.service';
import { CommonService } from 'src/app/services/common.service';

@Component({
    selector: 'app-withdrawal-limit-input',
    templateUrl: './withdrawal-limit-input.component.html',
    styleUrl: './withdrawal-limit-input.component.sass',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => WithdrawalLimitInputComponent),
            multi: true
        }
    ],
    standalone: false
})
export class WithdrawalLimitInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  _supportedCurrencies: Currency[] = [];
  value: WithdrawalLimit[] = [];
  withdrawalLimitTypeEnum = WithdrawalLimitType;
  allTypes = Object.keys(WithdrawalLimitType);
  channels: WithdrawalChannel[] = [];

  public constructor(public languageService: LanguageService, private currencyService: CurrencyService,
    private http: HttpClient, private restService: RestService, private commonService: CommonService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
    let headers = this.restService.initRequestHeaders();
    const requestBody = this.commonService.buildPostStringBody({pageSize: -1, pageIndex: -1});
    this.http.post<GeneralApiResponse>(environment.apiUrl.withdrawalChannel + "/", requestBody, { headers}).subscribe({
        next: (data: GeneralApiResponse) => {
            if (data.status === 0) {
                this.channels = (data.result as WithdrawalChannel[]);
            } else console.log(data);
        }, error: (data: any) => console.log(data)
    });
  }

  writeValue(value: WithdrawalLimit[]): void {
    this.value = value;
  }
 
  registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.propagateTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }

  propagateChange = (_: WithdrawalLimit[]) => { };
  propagateTouched = (_: WithdrawalLimit[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: Currency[]) {
    this._supportedCurrencies = supportedCurrencies;
  }

  get supportedCurrencies(): Currency[] {
    return this._supportedCurrencies;
  }

  limitCurrencyChanged($event: any, limit: WithdrawalLimit) {
  }

  addLimitClick($event: MouseEvent) {
    if (this._supportedCurrencies.length > 0) {
      var currency = this._supportedCurrencies[0];
      if (this._supportedCurrencies.length > 1) {
        currency = this._supportedCurrencies[1];
      }
      if (this.value.length > 0) {
        const last = this.value[this.value.length - 1];
        for (const c of this._supportedCurrencies) {
          if (c.id == last.currencyId) {
            currency = c;
            break;
          }
        }
      }
      var item = new WithdrawalLimit();
      item.currencyId = currency.id;
      if (this.value.length > 0) {
        item.channelId = this.value[this.value.length - 1].channelId;
      } else if (this.channels.length > 0) {
        item.channelId = this.channels[0].id;
      }
      this.value.push(item);
    }
  }

  removeLimitClick($event: MouseEvent, index: number) {
    this.value.splice(index, 1);
  }
}
