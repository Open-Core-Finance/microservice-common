import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { IndividualCustomer } from 'src/app/classes/customers/IndividualCustomer';
import { environment } from 'src/environments/environment';
import { ExpansionPanelInputGroup, UiFormBigHeader, UiFormCheckbox, UiFormComplexInput, UiFormDate, UiFormInput, UiFormItem, UiFormSelect, UiSelectItem } from 'src/app/classes/ui/UiFormInput';
import { UiFormDivider } from 'src/app/classes/ui/UiFormInput';
import { Gender } from 'src/app/classes/Gender';
import { MaritalStatus } from 'src/app/classes/customers/MaritalStatus';
import { Nationality } from 'src/app/classes/customers/Nationality';
import { CustomerIdentityType } from 'src/app/classes/customers/CustomerIdentityType';
import { GeneralAddCustomerComponent } from '../general-add-customer.component';
import {MatExpansionModule} from '@angular/material/expansion';
import { CryptoAccountComponent } from 'src/app/accounts/crypto-account/crypto-account.component';
import { DepositAccountComponent } from 'src/app/accounts/deposit-account/deposit-account.component';
import { LoanAccountComponent } from 'src/app/accounts/loan-account/loan-account.component';

@Component({
  selector: 'app-add-individual-customer',
  standalone: true,
  imports: [CommonModule, SharedModule, MatExpansionModule, CryptoAccountComponent, 
    DepositAccountComponent, LoanAccountComponent],
  templateUrl: './add-individual-customer.component.html',
  styleUrl: './add-individual-customer.component.sass'
})
export class AddIndividualCustomerComponent extends GeneralAddCustomerComponent<IndividualCustomer> {

  nationalityIdentityTypeItems: UiSelectItem[] = Object.keys(CustomerIdentityType).map( t =>
    ({labelKey: "nationality.identityType_" + t, selectValue: t} as UiSelectItem)
  );

  protected override getServiceUrl(): string {
    return environment.apiUrl.individualCustomer;
  }

  protected override validateFormData(formData: any): void {
    const errors = this.message['error'];
    if (this.commonService.isNullOrEmpty(formData.contactEmail)) {
      errors.push("contact_email_empty")
    }
  }

  protected override newEmptyEntity(): IndividualCustomer {
    return new IndividualCustomer();
  }

  protected override buildFormItems(): UiFormItem[] {
    const that = this;
    const formItems: UiFormItem[] = [];
    let prefix = "individualCustomer.";
    // Generic info
    formItems.push(new UiFormInput(prefix + "title", "title"));
    formItems.push(new UiFormInput(prefix + "firstName", "firstName"));
    formItems.push(new UiFormInput(prefix + "middleName", "middleName"));
    formItems.push(new UiFormInput(prefix + "lastName", "lastName"));
    formItems.push(new UiFormSelect(prefix + "gender",
      Object.keys(Gender).map( s => ({labelKey: "user.gender_" + s, selectValue: s} as UiSelectItem))
    , "gender"));
    formItems.push(new UiFormInput(prefix + "cisNumber", "cisNumber", "text"));
    formItems.push(new UiFormInput(prefix + "placeOfBirth", "placeOfBirth"));
    formItems.push(new UiFormDate(prefix + "dob", "dob"));
    formItems.push(new UiFormSelect(prefix + "maritalStatus",
      Object.keys(MaritalStatus).map( s => ({labelKey: "maritalStatus." + s, selectValue: s} as UiSelectItem))
    , "maritalStatus"));
    
    // Contact info
    formItems.push(new UiFormDivider());
    formItems.push(new UiFormBigHeader(() => that.languageService.formatLanguage(prefix + "contactInfo")));
    formItems.push(new UiFormInput(prefix + "contactPhone", "contactPhone"));
    formItems.push(new UiFormInput(prefix + "contactHomePhone", "contactHomePhone"));
    formItems.push(new UiFormInput(prefix + "contactEmail", "contactEmail"));
    formItems.push(new UiFormInput(prefix + "contactCompanyPhone", "contactCompanyPhone"));
    formItems.push(new UiFormSelect(prefix + "country", this.buildListSelection("countryId"), "countryId"));
    formItems.push(new UiFormSelect(prefix + "state", this.buildListSelection("stateId"), "stateId"));
    formItems.push(new UiFormSelect(prefix + "city", this.buildListSelection("cityId"), "cityId"));
    formItems.push(new UiFormInput(prefix + "district", "district"));
    formItems.push(new UiFormInput(prefix + "streetAddressLine1", "streetAddressLine1"));
    formItems.push(new UiFormInput(prefix + "streetAddressLine2", "streetAddressLine2"));
    formItems.push(new UiFormInput(prefix + "zipPostalCode", "zipPostalCode"));

    // Mailing info
    formItems.push(new UiFormDivider());
    formItems.push(new UiFormBigHeader(() => that.languageService.formatLanguage(prefix + "mailingInfo")));
    formItems.push(new UiFormCheckbox(prefix + "mailingSameWithAddress", "mailingSameWithAddress"));
    formItems.push(new UiFormSelect(prefix + "mailingCountry", this.buildListSelection("mailingCountryId"), "mailingCountryId",
      null, () => that.addForm && that.addForm.value.mailingSameWithAddress));
    formItems.push(new UiFormSelect(prefix + "mailingState", this.buildListSelection("mailingStateId"), "mailingStateId",
      null, () => that.addForm && that.addForm.value.mailingSameWithAddress));
    formItems.push(new UiFormSelect(prefix + "mailingCity", this.buildListSelection("mailingCityId"), "mailingCityId",
      null, () => that.addForm && that.addForm.value.mailingSameWithAddress));
    formItems.push(new UiFormInput(prefix + "mailingDistrict", "mailingDistrict", undefined,
      null, () => that.addForm && that.addForm.value.mailingSameWithAddress));
    formItems.push(new UiFormInput(prefix + "mailingStreetAddressLine1", "mailingStreetAddressLine1", undefined,
      null, () => that.addForm && that.addForm.value.mailingSameWithAddress));
    formItems.push(new UiFormInput(prefix + "mailingStreetAddressLine2", "mailingStreetAddressLine2", undefined,
      null, () => that.addForm && that.addForm.value.mailingSameWithAddress));
    formItems.push(new UiFormInput(prefix + "mailingZipPostalCode", "mailingZipPostalCode", undefined,
      null, () => that.addForm && that.addForm.value.mailingSameWithAddress));
    
    // Data sharing
    formItems.push(new UiFormDivider());
    formItems.push(new UiFormBigHeader(() => that.languageService.formatLanguage(prefix + "dataShare")));
    formItems.push(new UiFormCheckbox(prefix + "consentMarketing", "consentMarketing"));
    formItems.push(new UiFormCheckbox(prefix + "consentNonMarketing", "consentNonMarketing"));
    formItems.push(new UiFormCheckbox(prefix + "consentAbroad", "consentAbroad"));
    formItems.push(new UiFormCheckbox(prefix + "consentTransferToThirdParty", "consentTransferToThirdParty"));
    formItems.push(new UiFormDivider());

    formItems.push(new UiFormCheckbox(prefix + "singleNationality", "singleNationality"));

    return formItems;
  }

  protected override buildFormInputGroups(): ExpansionPanelInputGroup[] {
    const that = this;
    var result: ExpansionPanelInputGroup[] = [];
    let prefix = "individualCustomer.";
    // nationality
    let formItems:UiFormItem[] = [];
    formItems.push(new UiFormComplexInput("nationality", "nationality"));
    result.push(new ExpansionPanelInputGroup(prefix + 'nationality', formItems));
    // nationality
    formItems = [];
    formItems.push(new UiFormComplexInput("secondNationality", "secondNationality"));
    result.push(new ExpansionPanelInputGroup(prefix + 'secondNationality', formItems,
      () => !that.addForm || !that.addForm.value.singleNationality));
    // Return
    return result;
  }

  protected override get additionalFormGroupElement(): any {
    return {
      nationality: this.formBuilder.group(new Nationality()),
      secondNationality: this.formBuilder.group(new Nationality())
    };
  }
}
