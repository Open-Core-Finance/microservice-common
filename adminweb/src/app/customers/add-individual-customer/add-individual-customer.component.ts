import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';
import { IndividualCustomer } from 'src/app/classes/customers/IndividualCustomer';
import { environment } from 'src/environments/environment';
import { FormGroup } from '@angular/forms';
import { ExpansionPanelInputGroup, UiFormBigHeader, UiFormCheckbox, UiFormDate, UiFormInput, UiFormItem, UiFormSelect, UiSelectItem } from 'src/app/classes/ui/UiFormInput';
import { UiFormDivider } from 'src/app/classes/ui/UiFormInput';
import { Gender } from 'src/app/classes/Gender';
import { MaritalStatus } from 'src/app/classes/customers/MaritalStatus';

@Component({
  selector: 'app-add-individual-customer',
  standalone: true,
  imports: [CommonModule, SharedModule],
  templateUrl: './add-individual-customer.component.html',
  styleUrl: './add-individual-customer.component.sass'
})
export class AddIndividualCustomerComponent extends GeneralEntityAddComponent<IndividualCustomer> implements OnDestroy, OnInit {  
  addForm = this.formBuilder.group(this.newEmptyEntity());

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.organization;
  }

  protected override getAddForm(): FormGroup<any> {
    return this.addForm;
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
    formItems.push(new UiFormInput(prefix + "streetAddressLine1", "streetAddressLine1"));
    formItems.push(new UiFormInput(prefix + "streetAddressLine2", "streetAddressLine2"));
    formItems.push(new UiFormInput(prefix + "district", "district"));
    formItems.push(new UiFormInput(prefix + "city", "city"));
    formItems.push(new UiFormInput(prefix + "state", "state"));
    formItems.push(new UiFormInput(prefix + "country", "country"));
    formItems.push(new UiFormInput(prefix + "zipPostalCode", "zipPostalCode"));

    // Mailing info
    formItems.push(new UiFormDivider());
    formItems.push(new UiFormBigHeader(() => that.languageService.formatLanguage(prefix + "mailingInfo")));
    formItems.push(new UiFormInput(prefix + "mailingStreetAddressLine1", "mailingStreetAddressLine1"));
    formItems.push(new UiFormInput(prefix + "mailingStreetAddressLine2", "mailingStreetAddressLine2"));
    formItems.push(new UiFormInput(prefix + "mailingDistrict", "mailingDistrict"));
    formItems.push(new UiFormInput(prefix + "mailingCity", "mailingCity"));
    formItems.push(new UiFormInput(prefix + "mailingState", "mailingState"));
    formItems.push(new UiFormInput(prefix + "mailingCountry", "mailingCountry"));
    formItems.push(new UiFormInput(prefix + "mailingZipPostalCode", "mailingZipPostalCode"));
    
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

  protected override buildFormInputGroups(): ExpansionPanelInputGroup[] {
    return [];
  }
}