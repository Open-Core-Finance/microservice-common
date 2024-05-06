import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { environment } from 'src/environments/environment';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { ExpansionPanelInputGroup, UiFormInput, UiFormItem, UiFormSelect, UiSelectItem } from 'src/app/classes/ui/UiFormInput';
import { CommonModule } from '@angular/common';
import { InternalFundTransfer } from 'src/app/classes/transactions/InternalFundTransfer';
import { AccountType } from 'src/app/classes/accounts/AccountType';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { BehaviorSubject } from 'rxjs';
import { LanguageService } from 'src/app/services/language.service';
import { CommonService } from 'src/app/services/common.service';
import { RestService } from 'src/app/services/rest.service';
import { HttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { OrganizationService } from 'src/app/services/organization.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { AbstractCustomerService, CorporateCustomerService, InvidualCustomerService } from 'src/app/services/customer.service';
import { CustomerType } from 'src/app/classes/customers/CustomerType';
import { AbstractDepositAccountService, CryptoAccountService, DepositAccountService } from 'src/app/services/account.service';
import { Account } from 'src/app/classes/accounts/Account';


@Component({
  selector: 'app-fund-transfer',
  standalone: true,
  imports: [CommonModule, SharedModule],
  templateUrl: './fund-transfer.component.html',
  styleUrl: './fund-transfer.component.sass'
})
export class FundTransferComponent extends GeneralEntityAddComponent<InternalFundTransfer> implements OnDestroy {

  fromCustomersObservable : undefined | BehaviorSubject<UiSelectItem[]>;
  toCustomersObservable : undefined | BehaviorSubject<UiSelectItem[]>;
  fromAccounts: Account[] = [];
  toAccounts: Account[] = [];
  toAccountsObservable : BehaviorSubject<UiSelectItem[]>;
  selectedFromAccount: Account | null = null;
  selectedToAccount: Account | null = null;

  constructor(public override languageService: LanguageService, protected override commonService: CommonService,
    protected override restService: RestService, protected override http: HttpClient, protected override formBuilder: FormBuilder,
    protected override organizationService: OrganizationService, protected override changeDetector: ChangeDetectorRef,
    protected override authenticationService: AuthenticationService,
    private invidualCustomerService: InvidualCustomerService, private corporateCustomerService: CorporateCustomerService,
    private depositAccountService: DepositAccountService, private cryptoAccountService: CryptoAccountService) {
      super(languageService, commonService, restService, http, formBuilder, organizationService, changeDetector, authenticationService);
      this.toAccountsObservable = new BehaviorSubject<UiSelectItem[]>([]);
  }

  protected override buildFormItems(): UiFormItem[] {
    return [];
  }

  protected override buildFormInputGroups(): ExpansionPanelInputGroup[] {
    const that = this;
    this.fromCustomersObservable = new BehaviorSubject<UiSelectItem[]>([]);
    this.toCustomersObservable = new BehaviorSubject<UiSelectItem[]>([]);
    var result: ExpansionPanelInputGroup[] = [];
    let prefix = "internalFundTransfer.";

    // Sender tab
    let formItems:UiFormItem[] = [];
    result.push(new ExpansionPanelInputGroup(prefix + 'sender', formItems));
    formItems.push(new UiFormSelect("customerType.type", that.buildListSelection("fromCustomerType"), "fromCustomerType"));
    var item: UiFormItem = new UiFormInput(prefix + "fromCustomer", "fromCustomerId");
    (item as UiFormInput).autoComleteItems = that.fromCustomersObservable;
    formItems.push(item);
    formItems.push(new UiFormSelect( prefix + "fromAccountType", [AccountType.DEPOSIT, AccountType.CRYPTO].map( t => (
      {selectValue: t, labelKey: "accountType_" + t} as UiSelectItem
    )), "fromAccountType", () => this.addForm.value.fromCustomerId != ''));
    formItems.push(new UiFormSelect( prefix + "fromAccount", that.buildListSelection("fromAccountId"), "fromAccountId", () => this.addForm.value.fromCustomerId != ''));
    formItems.push(new UiFormSelect( prefix + "fromCurrency", that.buildListSelection("fromCurrency"), "fromCurrency",
      () => this.addForm.value.fromCustomerId != '' && this.selectedFromAccount != null));
    formItems.push(new UiFormInput(prefix + "fromAmount", "fromAmount", "number", () => this.addForm.value.fromCustomerId != '' && this.selectedFromAccount != null));

    // Receiver
    formItems = [];
    result.push(new ExpansionPanelInputGroup(prefix + 'receiver', formItems));
    formItems.push(new UiFormSelect("customerType.type", this.buildListSelection("toCustomerType"), "toCustomerType"));
    item = new UiFormInput(prefix + "toCustomer", "toCustomerId");
    (item as UiFormInput).autoComleteItems = that.toCustomersObservable;
    formItems.push(item);
    formItems.push(new UiFormSelect( prefix + "toAccountType", [AccountType.DEPOSIT, AccountType.CRYPTO].map( t => (
      {selectValue: t, labelKey: "accountType_" + t} as UiSelectItem
    )), "toAccountType", () => this.addForm.value.toCustomerId != ''));

    formItems.push(new UiFormSelect( prefix + "toAccount", that.buildListSelection("toAccountId"), "toAccountId", () => this.addForm.value.toCustomerId != ''));

    // Return
    return result;
  }

  ngOnDestroy(): void {
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.internalFundTransfer;
  }

  protected override validateFormData(formData: any): void {
    if (this.commonService.isNullOrEmpty(formData.fromAccountId)) {
      this.message['error'].push("from_account_id_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.toAccountId)) {
      this.message['error'].push("to_account_id_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.fromAccountType)) {
      this.message['error'].push("from_account_type_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.toAccountType)) {
      this.message['error'].push("to_account_type_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.fromCurrency)) {
      this.message['error'].push("from_currency_empty")
    }
    if (formData.fromAmount <= 0) {
      this.message['error'].push("invalid_from_amount")
    }
    if (isNaN(Number(formData.fromCustomerId.toString()))) {
      if (this.commonService.isNullOrEmpty(formData.fromCustomerId)) {
        this.message['error'].push("from_customer_empty")
      } else {
        var index = formData.fromCustomerId.indexOf("-");
        if (index > 0) {
          formData.fromCustomerId = formData.fromCustomerId.substring(index + 1, formData.fromCustomerId.length).trim();
        }
      }
    }
    if (isNaN(Number(formData.toCustomerId.toString()))) {
      if (this.commonService.isNullOrEmpty(formData.toCustomerId)) {
        this.message['error'].push("to_customer_empty")
      } else {
        var index = formData.toCustomerId.indexOf("-");
        if (index > 0) {
          formData.toCustomerId = formData.toCustomerId.substring(index + 1, formData.toCustomerId.length).trim();
        }
      }
    }
  }

  protected override newEmptyEntity(): InternalFundTransfer {
    return new InternalFundTransfer();
  }

  protected override resetClick($event: any) {
      super.resetClick($event);
  }

  fieldInput($event: any) {
    const that = this;
    if ($event.name == 'fromCustomerId' || $event.name == 'toCustomerId') {
      const filterText: string = $event.value;
      let customerType: CustomerType;
      let observable: undefined | BehaviorSubject<UiSelectItem[]>;
      if ($event.name == 'toCustomerId') {
        customerType = that.addForm.value.toCustomerType;
        observable = that.toCustomersObservable;
        this.addForm.patchValue({toAccountId: ""});
        this.toAccounts.splice(0, this.toAccounts.length);
        this.selectedToAccount = null;
      } else {
        customerType = that.addForm.value.fromCustomerType;
        observable = that.fromCustomersObservable;
        this.addForm.patchValue({fromAccountId: ""});
        this.fromAccounts.splice(0, this.fromAccounts.length);
        this.selectedFromAccount = null;
      }
      var customerService: AbstractCustomerService = (customerType == CustomerType.INDIVIDUAL) ? that.invidualCustomerService : that.corporateCustomerService;
      customerService.filterCustomer(filterText).subscribe( r => {
        var customers: any[] = r.result;
        if (customers) {
          const val = customers.map( m => {
            const label = m[customerService.autocompleteAttr] + " - " + m.id;
            return ({selectValue: label, labelKey: label} as UiSelectItem);
          });
          observable?.next(val);
        } else {
          observable?.next([]);
        }
      });
    }
  }

  protected override buildListSelection(selectName: string): UiSelectItem[] {
    if (selectName == 'fromCustomerType' || selectName == 'toCustomerType') {
      return Object.keys(CustomerType).map( m => ({ selectValue: m, labelKey: "customerType.type_" + m} as UiSelectItem));
    } else if (selectName == 'fromAccountId') {
      if (this.fromAccounts) {
        return this.fromAccounts.map( m => ({ selectValue: m.id, labelKey: m.id} as UiSelectItem));
      } else {
        return [];
      }
    } else if (selectName == 'toAccountId') {
      if (this.toAccounts) {
        return this.toAccounts.map( m => ({ selectValue: m.id, labelKey: m.id} as UiSelectItem));
      } else {
        return [];
      }
    } else if (selectName == 'fromCurrency') {
      if (this.selectedFromAccount) {
        return this.selectedFromAccount.supportedCurrencies.map( m => ({ selectValue: m, labelKey: m} as UiSelectItem));
      } else {
        return [];
      }
    }
    return super.buildListSelection(selectName);
  }

  onOptionsSelected($event: any) {
    var that = this;
    if ($event.name == 'fromCustomerId' || $event.name == 'toCustomerId') {
      const customerIdStr = $event.value;
      that.refreshAccountList($event.name, customerIdStr);
    } else if ($event.name == 'fromAccountType' || $event.name == 'toAccountType') {
      var customerIdStr: string;
      var customerEvent: string;
      if ($event.name == 'fromAccountType') {
        customerIdStr = this.addForm.value.fromCustomerId;
        customerEvent = "fromCustomerId";
      } else {
        customerIdStr = this.addForm.value.toCustomerId;
        customerEvent = "toCustomerId";
      }
      that.refreshAccountList(customerEvent, customerIdStr);
    } else if ($event.name == 'fromAccountId') {
      let accountId = this.addForm.value.fromAccountId;
      let account: Account | null = null;
      for (let a of this.fromAccounts) {
        if (a.id == accountId) {
          account = a;
          break;
        }
      }
      this.selectedFromAccount = account;
      this.updateSelectItem("fromCurrency");
    } else if ($event.name == 'toAccountId') {
      let accountId = this.addForm.value.toAccountId;
      let account: Account | null = null;
      for (let a of this.toAccounts) {
        if (a.id == accountId) {
          account = a;
          break;
        }
      }
      this.selectedToAccount = account;
    }
  }

  refreshAccountList(customerEvent: string, customerIdStr: string) {
    var that = this;
    let accountType: AccountType;
    let listAccounts: Account[];
    let selectedToUpdate: string;
    let customerType: CustomerType;
    if (customerEvent == 'fromCustomerId') {
      accountType = that.addForm.value.fromAccountType;
      listAccounts = this.fromAccounts;
      selectedToUpdate = "fromAccountId";
      customerType = that.addForm.value.fromCustomerType;
      this.selectedFromAccount = null;
    } else {
      accountType = that.addForm.value.toAccountType;
      listAccounts = this.toAccounts;
      selectedToUpdate = "toAccountId";
      customerType = that.addForm.value.toCustomerType;
      this.selectedToAccount = null;
    }
    let customerId = 0;
    var index = customerIdStr.indexOf("-");
    if (index > 0) {
      customerId = parseInt(customerIdStr.substring(index + 1).trim());
    }
    var depositAccountService: AbstractDepositAccountService = (accountType == AccountType.DEPOSIT) ? that.depositAccountService : that.cryptoAccountService;
    listAccounts.splice(0, listAccounts.length);
    if (!isNaN(customerId)) {
      depositAccountService.filterAccount('', customerId, customerType).subscribe( r => {
        var accounts: Account[] = r.result;
        if (accounts) {
          for (var i = 0; i < accounts.length; i++) {
            listAccounts.push(accounts[i]);
          }
          that.updateSelectItem(selectedToUpdate);
        }
      });
    }
  }
}