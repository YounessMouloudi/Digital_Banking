import { Component, OnInit } from '@angular/core';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { CustomersService } from '../services/customers.service';
import { AuthService } from '../services/auth.service';
import { AccountCustomer } from '../model/account.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customer-profile',
  templateUrl: './customer-profile.component.html',
  styleUrls: ['./customer-profile.component.css']
})
export class CustomerProfileComponent implements OnInit {

  customerProfile! : Observable<AccountCustomer[]>;
  currentAcc! : AccountCustomer;
  savingAcc! : AccountCustomer;
  email! : string;

  constructor(private customerService : CustomersService,public authService : AuthService,
    private router : Router) { }

  ngOnInit(): void {
      
    this.email = this.authService.email;
    
    this.customerProfile = this.customerService.getCustomerProfile(this.email).pipe(
      tap((data) => {
        this.currentAcc = data[0];
        this.savingAcc = data[1];
      }),
      catchError(err => {
        // console.log(err);
        if(err.status == 0) {
          alert("Unable to connect to the server. Please check if the database is accessible");
          this.authService.logOut();
        }
        else {
          if(this.authService.roles != "USER") {
            alert("You Are Not Authorized To Access This Page !!!");
            this.router.navigateByUrl('/digitalBank');
          }      
        }
        return throwError(() => new Error(err));
      })
    )
  }

  handleEditProfile(id : number) {
    this.router.navigateByUrl("/digitalBank/edit/customer/"+id);
  }

  handleTransfer(id : string) {
    this.router.navigateByUrl("/digitalBank/accounts",{state : {id}});
  }
}
