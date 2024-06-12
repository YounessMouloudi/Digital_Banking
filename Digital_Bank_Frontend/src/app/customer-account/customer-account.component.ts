import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from '../model/customer.model';
import { AccountsService } from '../services/accounts.service';
import { Observable, catchError, map, throwError } from 'rxjs';
import { AccountCustomer, AccountDetails } from '../model/account.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-customer-account',
  templateUrl: './customer-account.component.html',
  styleUrls: ['./customer-account.component.css']
})
export class CustomerAccountComponent implements OnInit {
  
  customerId! : string;
  customer! : Customer;
  customerAccounts! : Observable<AccountCustomer[]>;
  errorMessage : string = "";

  constructor(private accountService : AccountsService, private route : ActivatedRoute, private router : Router,
    private authService : AuthService) {
    // hna ghadi nrécupiriw state li seftna m3a url w li howa un objet de type Customer
    this.customer = this.router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
    // hna récupirina l id mn url li jayin mno 
    this.customerId = this.route.snapshot.params['id'];

    // this.accountService.getCustomerAccount(this.customerId).subscribe({
    //   next : (data) => {
    //     console.log(data);
    //     this.customerAccounts = data
    //   },
    //   error : (err) => {
    //     this.errorMessage = err;
    //   }
    // })
    this.customerAccounts = this.accountService.getCustomerAccount(this.customerId).pipe(
      
      /* had map kont dart biha sort dial account by Type hadi coté Frontend mais aprés dartha tjini mn backend 
      mssortia 3awnt chwia b chatGpt hit ma3amri dart sort f angular*/
      /* map(accounts => {
        return accounts.sort((a, b) => a.type.localeCompare(b.type));
        // return accounts.sort((a, b) => a.id - b.id);
      }),*/

      catchError( err => {
        if(err.status = 0) {
          alert("Unable to connect to the server. Please check if the database is accessible");
          this.authService.logOut();
        }
        this.errorMessage = err.message;
        return throwError(() => new Error(err));
      })
    );
  }

  /* hna ghadi nseft state li ghaykon fih id f url dial accounts w ghadi nrécupéri id mn tema bach n9dar 
  ndir 3lih des opérations */
  handleTransaction(id : string){
    this.router.navigateByUrl("/digitalBank/accounts",{state : {id}});
  }
}
