import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {AsyncPipe} from '@angular/common';

import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule } from '@angular/material/dialog';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import {MatDividerModule} from '@angular/material/divider';
import {MatListModule} from '@angular/material/list';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSidenavModule} from '@angular/material/sidenav';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './header/header.component';
import { LeftMenuComponent } from './left-menu/left-menu.component';
import { SettingsComponent } from './settings/settings.component';
import { OrganizationComponent } from './system/organization/organization.component';
import { RoleComponent } from './system/role/role.component';
import { AddOrganizationComponent } from './system/add-organization/add-organization.component';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { CurrenyComponent } from './system/curreny/curreny.component';
import { AddCurrenyComponent } from './system/add-curreny/add-curreny.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HeaderComponent,
    LeftMenuComponent,
    SettingsComponent,
    OrganizationComponent,
    AddOrganizationComponent,
    ConfirmDialogComponent,
    CurrenyComponent,
    RoleComponent, AddCurrenyComponent
  ],
  imports: [
    BrowserModule, FormsModule, ReactiveFormsModule,
    AppRoutingModule, HttpClientModule,
    BrowserAnimationsModule,
    MatButtonModule, MatInputModule, MatMenuModule, MatIconModule, MatCardModule, MatSelectModule,
    MatExpansionModule, MatPaginatorModule, MatTableModule, MatSortModule, MatProgressSpinnerModule,
    MatDialogModule, MatAutocompleteModule, MatDatepickerModule, MatNativeDateModule, MatDividerModule,
    MatListModule, MatToolbarModule, MatSidenavModule, AsyncPipe
  ],
  providers: [],
  bootstrap: [AppComponent],
  exports: [
    MatButtonModule, MatInputModule
  ]
})
export class AppModule { }
