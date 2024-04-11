import { ChangeDetectorRef, Component, EventEmitter, Input, Output } from "@angular/core";
import { UserMessage } from "../classes/UserMessage";
import { LanguageService } from "../services/language.service";
import { CommonService } from "../services/common.service";
import { RestService } from "../services/rest.service";
import { HttpClient } from "@angular/common/http";
import { GeneralApiResponse } from "../classes/GeneralApiResponse";
import { FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { ExpansionPanelInputGroup, UiFormItem, UiFormSelect, UiSelectItem } from "../classes/ui/UiFormInput";
import { AuthenticationService } from "../services/authentication.service";
import { OrganizationService } from "../services/organization.service";

@Component({
    template: ''
})
export abstract class GeneralEntityAddComponent<T extends any> {
  @Output() cancel = new EventEmitter();
  @Output() save = new EventEmitter();
  _addingItem: T | null = null;
  message = new UserMessage([], []);
  formItems: UiFormItem[];
  inputGroups: ExpansionPanelInputGroup[] = [];

  addForm: FormGroup;

  constructor(public languageService: LanguageService, protected commonService: CommonService,
    protected restService: RestService, protected http: HttpClient, protected formBuilder: FormBuilder,
    protected organizationService: OrganizationService, protected changeDetector: ChangeDetectorRef,
    protected authenticationService: AuthenticationService) {
      this.addForm = this.buildAddForm();
      this.formItems = this.buildFormItems();
      this.inputGroups = this.buildFormInputGroups();
  }

  protected buildAddForm(): FormGroup {
    return this.formBuilder.group(
      Object.assign(Object.assign({}, this.newEmptyEntity()), this.additionalFormGroupElement)
    );
  }

  protected buildFormItems(): UiFormItem[] {
    return [];
  }

  protected buildFormInputGroups(): ExpansionPanelInputGroup[] {
    return [];
  }

  protected cancelClick($event: any): any {
    if (this.cancel) {
      this.cancel.emit($event);
    }
  }

  protected clearMessages() {
    this.message.clearAll();
  }

  protected abstract getServiceUrl(): string;

  protected abstract validateFormData(formData: any): void;

  protected abstract newEmptyEntity(): T;

  saveClick($event: any): any {
    this.clearMessages();
    const formData = this.addForm.value;
    this.validateFormData(formData);
    if (this.canDeleteFormId(formData.id)) {
      delete formData.id;
    }
    if (this.message['error'].length < 1) {
      this.processingDataBeforeSubmit(formData);
      const requestHeaders = this.restService.initApplicationJsonRequestHeaders();
      if (formData.id) {
        const serviceUrl = this.getServiceUrl() + "/" + formData.id;
        this.http.put<GeneralApiResponse>(serviceUrl, formData, {
          headers: requestHeaders, params: {}
        }).subscribe({
          next: (data: GeneralApiResponse) => {
            if (this.save) {
              var outputEvent = {
                entity: data.result,
                type: "updated"
              };
              this.save.emit(outputEvent);
            }
          }, error: (data: any) => this.restService.handleRestError(data, this.message)
        });
      } else {
        const serviceUrl = this.getServiceUrl() + "/create";
        this.http.post<GeneralApiResponse>(serviceUrl, formData, {
          headers: requestHeaders, params: {}
        }).subscribe({
          next: (data: GeneralApiResponse) => {
            if (this.save) {
              var outputEvent = {
                entity: data.result,
                type: "added"
              };
              this.save.emit(outputEvent);
            }
          }, error: (data: any) => this.restService.handleRestError(data, this.message)
        });
      }
    }
  }

  canDeleteFormId(id: any): boolean {
    if (typeof id == 'number') {
      return ((id as number) < 1)
    } else if (typeof id == 'string') {
      return this.commonService.isNullOrEmpty(id);
    }
    return false;
  }

  @Input() set addingItem(item: any| null) {
    this._addingItem = item;
    if (item) {
      this.addForm.setValue(item);
      this.afterBindingEnityToForm(false);
    } else {
      this.addForm.setValue(this.newEmptyEntity() as any);
      this.afterBindingEnityToForm(true);
    }
  }

  protected processingDataBeforeSubmit(formData: any): void {
  }

  protected afterBindingEnityToForm(isNew: boolean): void {
  }

  protected updateSelectItem(selectName: string) {
    for (let item of this.formItems) {
      if (item instanceof UiFormSelect) {
        var select = (item as UiFormSelect);
        if (select.formControlName == selectName) {
          select.selectItems = this.buildListSelection(selectName);
        }
      }
    }
  }

  protected buildListSelection(selectName: string): UiSelectItem[] {
    return [];
  }

  protected get additionalFormGroupElement(): any {
    return {};
  }
}