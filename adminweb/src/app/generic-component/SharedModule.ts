import { NgModule } from '@angular/core';
import { GeneralErrorPanelComponent } from './general-error-panel/general-error-panel.component';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { CommonModule } from '@angular/common';
import {MatDialogModule} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import { AutoResizeDirective } from './autoresize.directive';

@NgModule({
  declarations: [GeneralErrorPanelComponent, ConfirmDialogComponent, AutoResizeDirective],
  imports: [CommonModule, MatDialogModule, MatButtonModule],
  // export the component to make it available in other modules where the Shared module is imported
  exports: [GeneralErrorPanelComponent, ConfirmDialogComponent, AutoResizeDirective] 
})
export class SharedModule {  }