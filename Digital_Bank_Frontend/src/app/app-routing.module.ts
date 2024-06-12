import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { EditCustomerComponent } from './edit-customer/edit-customer.component';
import { CustomerAccountComponent } from './customer-account/customer-account.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { AuthenticationGuard } from './guards/authentication.guard';
import { AuthorizationGuard } from './guards/authorization.guard';
import { CustomerProfileComponent } from './customer-profile/customer-profile.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
  { path : "", redirectTo : "/login", pathMatch : "full"},
  { path : "login", component : LoginComponent},
  { path : "digitalBank", component : DashboardComponent, canActivate : [AuthenticationGuard], children : [
    { path : "", component : HomeComponent},
    { path : "customer-profile", component : CustomerProfileComponent},
    { path : "customers", component : CustomersComponent, canActivate : [AuthorizationGuard], data : {role : "ADMIN"} },
    { path : "accounts", component : AccountsComponent},
    { path : "new/customer", component : NewCustomerComponent, canActivate : [AuthorizationGuard], data : {role : "ADMIN"}},
    { path : "edit/customer/:id", component : EditCustomerComponent},
    { path : "customer-account/:id", component : CustomerAccountComponent, canActivate : [AuthorizationGuard], data : {role : "ADMIN"}},
    { path : "update-password", component : UpdatePasswordComponent},
  ]},
  { path : "**", component : NotFoundComponent, pathMatch : "full"}, // hna ghatkon page 404
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
