import { CommonModule } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

import {MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonModule} from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { User } from '../../classes/User';
import { Gender } from '../../classes/Gender';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';

@Component({
    selector: 'app-add-user',
    imports: [CommonModule, MatCardModule, FormsModule, ReactiveFormsModule, MatInputModule, MatSelectModule, SharedModule,
        MatButtonModule, MatFormFieldModule],
    templateUrl: './add-user.component.html',
    styleUrl: './add-user.component.sass'
})
export class AddUserComponent extends GeneralEntityAddComponent<User> implements OnDestroy {

  genderEnum = Gender;
  allGenders = Object.keys(Gender);

  ngOnDestroy(): void {
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.user;
  }

  protected override validateFormData(formData: any): void {
    const firstName = formData.firstName;
    if (this.commonService.isNullOrEmpty(firstName)) {
      this.message['error'].push("first_name_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.email)) {
      this.message['error'].push("email_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.username)) {
      this.message['error'].push("username_empty")
    }
  }
  protected override newEmptyEntity(): User {
    return new User();
  }

}
