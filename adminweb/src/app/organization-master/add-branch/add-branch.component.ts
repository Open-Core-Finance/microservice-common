import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Branch } from '../../classes/organizations/Branch';
import { FormControl, FormGroup} from "@angular/forms";
import { environment } from 'src/environments/environment';
import { DayOfWeek } from 'src/app/classes/DayOfWeek';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';

@Component({
  selector: 'app-add-branch',
  templateUrl: './add-branch.component.html',
  styleUrl: './add-branch.component.sass'
})
export class AddBranchComponent extends GeneralEntityAddComponent<Branch> {

  listDayOfWeeks = Object.keys(DayOfWeek);

  addBranchForm = this.formBuilder.group(Object.assign(
    Object.assign({}, new Branch()), {
      nonWorkingDays: new FormControl<DayOfWeek[]>([]),
      createdDate: new FormControl<any>(new Date()),
      lastModifiedDate: new FormControl<any>(new Date())
    }
  ));

  protected override getAddForm(): FormGroup<any> {
    return this.addBranchForm;
  }

  protected override validateFormData(formData: any): void {
    if (this.commonService.isNullOrEmpty(formData.name)) {
      this.message['error'].push("name_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.phoneNumber)) {
      this.message['error'].push("phone_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.email)) {
      this.message['error'].push("email_empty")
    }
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.branch;
  }

  protected override newEmptyEntity(): Branch {
    return new Branch();
  }

  isDayOfWeekChecked(dayOfWeekName: string) {
    const dayOfWeeks = this.addBranchForm.controls.nonWorkingDays.value;
    if (dayOfWeeks) {
      for (var dayOfWeek of dayOfWeeks) {
        if (dayOfWeek === (dayOfWeekName as DayOfWeek)) {
          return true;
        }
      }
    }
    return false;
  }

  dayOfWeekChanged(dayOfWeekName: string, event: MatCheckboxChange) {
    const dayOfWeeks = this.addBranchForm.controls.nonWorkingDays.value;
    if (dayOfWeeks) {
      if (event.checked == false) {
        for (let i = 0; i < dayOfWeeks.length; i++) {
          if ((dayOfWeekName as DayOfWeek) == dayOfWeeks[i]) {
            dayOfWeeks.splice(i, 1);
            i--;
          }
        }
      } else {
        dayOfWeeks.push(dayOfWeekName as DayOfWeek);
      }
    }
  }
}
