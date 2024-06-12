import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { EditCustomerComponent } from './edit-customer/edit-customer.component';
import { CustomerAccountComponent } from './customer-account/customer-account.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { AppHttpInterceptor } from './interceptors/app-http.interceptor';
import { CustomerProfileComponent } from './customer-profile/customer-profile.component';
import { NavbarComponent } from './navbar/navbar.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';
import { NotFoundComponent } from './not-found/not-found.component';

@NgModule({
  declarations: [
    AppComponent,
    CustomersComponent,
    AccountsComponent,
    DashboardComponent,
    NewCustomerComponent,
    EditCustomerComponent,
    CustomerAccountComponent,
    HomeComponent,
    LoginComponent,
    CustomerProfileComponent,
    NavbarComponent,
    UpdatePasswordComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [
    {provide : HTTP_INTERCEPTORS, useClass : AppHttpInterceptor, multi : true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
