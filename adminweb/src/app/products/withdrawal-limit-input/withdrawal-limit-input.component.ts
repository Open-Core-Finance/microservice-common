import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { Subscription } from 'rxjs';
import { DepositLimit } from 'src/app/classes/products/DepositLimit';
import { LanguageService } from 'src/app/services/language.service';
import { EntitiesService } from 'src/app/services/EntitiesService';
import { WithdrawalLimit, WithdrawalLimitType } from 'src/app/classes/products/WithdrawalLimit';
import { WithdrawalChannel } from 'src/app/classes/WithdrawalChannel';
import { HttpClient } from '@angular/common/http';
import { GeneralApiResponse } from 'src/app/classes/GeneralApiResponse';
import { environment } from 'src/environments/environment';
import { RestService } from 'src/app/services/rest.service';

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
  ]
})
export class WithdrawalLimitInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  lastSupportedCurrencies: string[] | undefined;
  currencies: Currency[] = [];
  currenciesToDisplay: Currency[] = [];
  currenciesSubscription: Subscription | undefined;
  value: WithdrawalLimit[] = [];
  withdrawalLimitTypeEnum = WithdrawalLimitType;
  allTypes = Object.keys(WithdrawalLimitType);
  channels: WithdrawalChannel[] = [];

  public constructor(public languageService: LanguageService, private entityService: EntitiesService, private http: HttpClient, private restService: RestService) {
    this.currenciesSubscription?.unsubscribe();
    this.currenciesSubscription = this.entityService.organizationObservableMap.get(EntitiesService.ENTITY_TYPE_CURRENCY)?.subscribe( c => {
      this.currencies = c;
      if (this.lastSupportedCurrencies) {
        this.populateCurrenciesToUi(this.lastSupportedCurrencies);
      }
    });
  }

  ngOnDestroy(): void {
    this.currenciesSubscription?.unsubscribe();
  }

  ngOnInit(): void {
    let headers = this.restService.initRequestHeaders();
    this.http.post<GeneralApiResponse>(environment.apiUrl.withdrawalChannel + "/", {pageSize: -1, pageIndex: -1}, { headers}).subscribe({
        next: (data: GeneralApiResponse) => {
            if (data.status === 0) {
                this.channels = (data.result as WithdrawalChannel[]);
            } else console.log(data);
        }, error: (data: any) => console.log(data)
    });
  }

  writeValue(value: WithdrawalLimit[]): void {
    this.value = value;
    this.populateCurrenciesToUi(this.lastSupportedCurrencies ? this.lastSupportedCurrencies : []);
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
  set supportedCurrencies(supportedCurrencies: string[]) {
    this.lastSupportedCurrencies = supportedCurrencies;
    this.populateCurrenciesToUi(supportedCurrencies);
  }

  private populateCurrenciesToUi(supportedCurrencies: string[]) {
    this.currenciesToDisplay = [];
    this.currencies.forEach((value, index, arr) => {
      for(let  i = 0; i < supportedCurrencies.length; i++) {
        const c = supportedCurrencies[i];
        if (c == value.id) {
          this.currenciesToDisplay.push(value);
          break;
        }
      }
    });
  }

  limitCurrencyChanged($event: any, limit: WithdrawalLimit) {
    for (const currency of this.currenciesToDisplay) {
      if ($event == currency.id) {
        limit.currencyName = currency.name;
        break;
      }
    }
  }

  addLimitClick($event: MouseEvent) {
    if (this.currenciesToDisplay.length > 0) {
      var currency = this.currenciesToDisplay[0];
      if (this.value.length > 0) {
        const last = this.value[this.value.length - 1];
        for (const c of this.currenciesToDisplay) {
          if (c.id == last.currencyId) {
            currency = c;
            break;
          }
        }
      }
      var item = new WithdrawalLimit();
      item.currencyId = currency.id;
      item.currencyName = currency.name;
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
