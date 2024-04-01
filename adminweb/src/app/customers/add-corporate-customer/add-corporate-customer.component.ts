import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneralAddCustomerComponent } from '../general-add-customer.component';
import { CorporateCustomer } from 'src/app/classes/customers/CorporateCustomer';
import { environment } from 'src/environments/environment';
import { UiFormBigHeader, UiFormCheckbox, UiFormDate, UiFormDivider, UiFormInput, UiFormItem, UiFormSelect } from 'src/app/classes/ui/UiFormInput';
import { SharedModule } from 'src/app/generic-component/SharedModule';

@Component({
  selector: 'app-add-corporate-customer',
  standalone: true,
  imports: [CommonModule, SharedModule],
  templateUrl: './add-corporate-customer.component.html',
  styleUrl: './add-corporate-customer.component.sass'
})
export class AddCorporateCustomerComponent extends GeneralAddCustomerComponent<CorporateCustomer> {

  protected override getServiceUrl(): string {
    return environment.apiUrl.corporateCustomer;
  }

  protected override validateFormData(formData: any): void {
    const errors = this.message['error'];
    if (this.commonService.isNullOrEmpty(formData.name)) {
      errors.push("name_empty")
    }
  }

  protected override newEmptyEntity(): CorporateCustomer {
    return new CorporateCustomer();
  }

  protected override buildFormItems(): UiFormItem[] {
    const that = this;
    const formItems: UiFormItem[] = [];
    let prefix = "corporateCustomer.";
    // Generic info
    formItems.push(new UiFormInput(prefix + "name", "name"));
    formItems.push(new UiFormInput(prefix + "taxNumber", "taxNumber"));
    formItems.push(new UiFormDate(prefix + "startDate", "startDate"));

    // Contact info
    formItems.push(new UiFormDivider());
    formItems.push(new UiFormBigHeader(() => that.languageService.formatLanguage(prefix + "contactInfo")));
    formItems.push(new UiFormInput(prefix + "contactPhone", "contactPhone"));
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
    return formItems;
  }

}