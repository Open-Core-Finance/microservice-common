import { CommonModule } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

import {MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonModule} from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Role } from '../../classes/Role';
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { GeneralEntityAddComponent } from 'src/app/generic-component/GeneralEntityAddComponent';

@Component({
  selector: 'app-add-commonRole',
  standalone: true,
  imports: [CommonModule, MatCardModule, FormsModule, ReactiveFormsModule, MatInputModule, MatSelectModule, SharedModule,
    MatButtonModule, MatFormFieldModule],
  templateUrl: './add-commonRole.component.html',
  styleUrl: './add-commonRole.component.sass'
})
export class AddRoleComponent extends GeneralEntityAddComponent<Role> implements OnDestroy {

  ngOnDestroy(): void {
  }

  protected override getServiceUrl(): string {
    return environment.apiUrl.commonRole;
  }

  protected override validateFormData(formData: any): void {
    if (this.commonService.isNullOrEmpty(formData.id)) {
      this.message['error'].push("id_empty")
    }
    if (this.commonService.isNullOrEmpty(formData.name)) {
      this.message['error'].push("name_empty")
    }
  }

  protected override newEmptyEntity(): Role {
    return new Role("", "", null, this.organizationService.organization);
  }

}
