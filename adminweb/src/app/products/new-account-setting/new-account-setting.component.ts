import { Component, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ControlValueAccessor, FormsModule, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { ProductNewAccountSetting, ProductNewAccountSettingType } from 'src/app/classes/products/Product';
import { LanguageService } from 'src/app/services/language.service';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import { AccountState } from 'src/app/classes/accounts/AccountState';
import {MatInputModule} from '@angular/material/input';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import {MatCheckboxModule} from '@angular/material/checkbox';

@Component({
  selector: 'app-new-account-setting',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, MatFormFieldModule, MatSelectModule, MatInputModule, SharedModule,
    MatCheckboxModule],
  templateUrl: './new-account-setting.component.html',
  styleUrl: './new-account-setting.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => NewAccountSettingComponent),
      multi: true
    }
  ]
})
export class NewAccountSettingComponent implements OnInit, ControlValueAccessor, OnDestroy {
  disabled: boolean = false;
  _value: ProductNewAccountSetting = new ProductNewAccountSetting();
  newAccountIdTypeEnum = ProductNewAccountSettingType;
  allAccountStates = Object.keys(AccountState);
  allNewAccountTypes = Object.keys(ProductNewAccountSettingType);

  public constructor(public languageService: LanguageService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  writeValue(value: ProductNewAccountSetting): void {
    this.value = value;
  }
 
  registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.propagateTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  propagateChange = (_: ProductNewAccountSetting[]) => { };
  propagateTouched = (_: ProductNewAccountSetting[]) => { };

  get value() : ProductNewAccountSetting {
    return this._value;
  }

  set value(v: ProductNewAccountSetting) {
    this._value = v;
  }
}
