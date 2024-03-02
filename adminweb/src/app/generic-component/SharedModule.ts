import { NgModule } from '@angular/core';
import { GeneralErrorPanelComponent } from './general-error-panel/general-error-panel.component';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { CommonModule } from '@angular/common';
import { MatDialogModule} from '@angular/material/dialog';
import { MatButtonModule} from '@angular/material/button';
import { AutoResizeDirective } from './autoresize.directive';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule} from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { ReactiveFormInputComponent } from './reactive-form-input/reactive-form-input.component';
import { ReactiveFormSelectComponent } from './reactive-form-select/reactive-form-select.component';
import { GenericFormComponent } from './generic-form/generic-form.component';

@NgModule({
  declarations: [
    GeneralErrorPanelComponent, ConfirmDialogComponent, AutoResizeDirective, ReactiveFormInputComponent,
    ReactiveFormSelectComponent, GenericFormComponent
  ],
  imports: [
    CommonModule, MatDialogModule, MatButtonModule, MatInputModule, MatIconModule, FormsModule, ReactiveFormsModule,
    MatFormFieldModule, MatSelectModule, MatCardModule
  ],
  // export the component to make it available in other modules where the Shared module is imported
  exports: [
    GeneralErrorPanelComponent, ConfirmDialogComponent, AutoResizeDirective, ReactiveFormInputComponent,
    ReactiveFormSelectComponent, GenericFormComponent
  ] 
})
export class SharedModule {  }