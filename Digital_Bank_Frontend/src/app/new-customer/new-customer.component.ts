import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomersService } from '../services/customers.service';
import { catchError, throwError } from 'rxjs';
import { Customer } from '../model/customer.model';
import { Router } from '@angular/router';
import { AccountsService } from '../services/accounts.service';



@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.css']
})
export class NewCustomerComponent implements OnInit {

  newCustomerForm! : FormGroup;
  errorMessage : string = "";
  initialBalance : number = 100; // le montant li ghaykon f l compte mnin ghaytcréa direct
  overDraft : number = 10000;  // overDraft càd découvert => ay ch7al l7ad l'a9ssa li n9dar nss7ab mno


  constructor(private fb : FormBuilder, public customerService : CustomersService,
    private router : Router, private accountService : AccountsService) { }

  ngOnInit(): void {
    this.newCustomerForm = this.fb.group({
      firstName : this.fb.control(null, [Validators.required, Validators.minLength(3)]),
      lastName : this.fb.control(null, [Validators.required, Validators.minLength(3)]),
      email : this.fb.control(null, [Validators.required, Validators.email]),
      phone : this.fb.control(null, [Validators.required, Validators.minLength(10), Validators.maxLength(10)]),
      cin : this.fb.control(null, Validators.required),
      address : this.fb.control(null, Validators.required),
    })
  }

  handleSaveCustomer() {

    let customer : Customer = this.newCustomerForm.value;
    
    this.customerService.saveCustomer(customer).subscribe({
      next : (data) => {
        alert("Customer created SuccessFully\n Customer Password is : "+data.password);
        
        this.newCustomerForm.reset();

        let customerId = data.id;

        this.handleSaveAccount(customerId);
        
        this.router.navigateByUrl("/digitalBank/customers");
      },
      error : (err) => {
        // console.log(err);
        if(err.status == 403) {
          this.errorMessage = "Forbiden. You Are Not Authorized To Access This Page.";
        }
        if(err.status == 401) {
          this.errorMessage = "NOT AUTHORIZED";
        }
        else {
          this.errorMessage = err.error.message;
        }
      }
    });

  };

  handleSaveAccount(customerId : number) {

    this.accountService.saveCurrentAccount(customerId).subscribe({
      next : () => {
        // console.log(data);
      },
      error : (err) => {
        this.errorMessage = err.error.message;
      }    
    });

    this.accountService.saveSavingAccount(customerId).subscribe({
      next : () => {
        // console.log(data);
      },
      error : (err) => {
        this.errorMessage = err.error.message;
      }    
    });
  }

    // had la methode tania ila bghiti t initialiser hadok 2 params lawlin hna w machi f service
    // this.accountService.saveCurrentAccount2(this.initialBalance,this.overDraft,customerId).subscribe({
    //   next : (data) => {
    //     console.log(data);
    //   },
    //   error : (err) => {
    //     this.errorMessage = err.message;
    //   }    
    // });

}

