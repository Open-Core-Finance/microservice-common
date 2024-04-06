import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule} from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatSortModule } from '@angular/material/sort';
import {MatDividerModule} from '@angular/material/divider';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { CurrenciesSelectionComponent } from './currencies-selection/currencies-selection.component';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatToolbarModule} from '@angular/material/toolbar';

@NgModule({
  declarations: [
    CurrenciesSelectionComponent
  ],
  imports: [
    CommonModule, MatInputModule, MatIconModule, FormsModule, ReactiveFormsModule,
    MatFormFieldModule, MatSelectModule, MatCardModule,
    MatSortModule, MatDividerModule, MatAutocompleteModule, MatExpansionModule,
    MatToolbarModule
  ],
  // export the component to make it available in other modules where the Shared module is imported
  exports: [
    CurrenciesSelectionComponent
  ] 
})
export class CurrencyModule {  }