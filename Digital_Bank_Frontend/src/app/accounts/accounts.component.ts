import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { AccountsService } from '../services/accounts.service';
import { EMPTY, Observable, catchError, map, tap, throwError } from 'rxjs';
import { AccountDetails } from '../model/account.model';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {

  accountForm! : FormGroup;
  currentPage : number = 0;
  sizePage : number = 2;
  accountObservable! : Observable<AccountDetails> | null;
  // had "accountObservable" ima taktabha haka aw taktabha haka "account$" => hit $ tat3ni Observable
  operationForm! : FormGroup;
  errorMessage : string = "";
  accountData: boolean = false;
  accountDestinationForm! : FormGroup;
  Balance! : number;
  accountIdSource! : string;
  isChecked: boolean = false;

  constructor(private fb : FormBuilder, public accountService : AccountsService, public authService : AuthService) { }

  ngOnInit(): void {
    /* hna dart wahd test w li howa ila kan dak state li fih id w li jay mn customer-account non null 
    accountId ghadi yakhod dik la valeur dial state sinon rah accountId ghaykon null 
    */
    this.accountForm = this.fb.group({
      accountId : history.state.id != null ? this.fb.control(history.state.id,[Validators.required, Validators.minLength(10)]) : this.fb.control('',[Validators.required, Validators.minLength(10)]),
      // accountId : ['',],
    });
    
    this.operationForm = this.fb.group({
      operationType : this.fb.control(null,[Validators.required]),
      // hna f amount zadt l pattern bach le champ amount y accepter ghir les numbers 
      amount : this.fb.control(0,[Validators.required, Validators.min(1),Validators.pattern(/^\d+$/)]),
      description : this.fb.control(''),
    });

    this.accountDestinationForm = this.fb.group({
      accountDestination : this.fb.control('',[Validators.required, Validators.minLength(10)])
    });

    /* hadi dartha bach ila khtar operationType = TRANSFER w saisa chi haja f accountDestination et aprés 3awd 
    khtar autre operationType le champ kan tayb9a 3amer alors drna hadi bach nkhwiw le champ */
    this.operationForm.get("operationType")?.valueChanges.subscribe((value : string) => {
      if (value !== 'TRANSFER') {
        this.accountDestinationForm.reset();
      }
    });
    
    if(this.authService.roles.includes('USER') && !this.authService.roles.includes('ADMIN')){
      this.operationForm.patchValue({operationType : "TRANSFER"});
    }

  }
  
  onInputChange(){

    if(!this.accountForm.value.accountId) {
      this.accountObservable = null;
      this.accountData = false;
      this.errorMessage = "";
      this.operationForm.reset();
      this.accountDestinationForm.reset();
    }
  }


  /* hna kont dayr un customer validator hit le champ accountDestination kont dayro f operationForm 
  aprés l9it ghadi ykhla9 lia des errors ktar w darto f form bohdo */
  // private accDestinationValidator(): ValidatorFn {
  //   return (control: AbstractControl): ValidationErrors | null => {

  //     const operationForm = this.operationForm;

  //     if (!operationForm) {
  //       return null;
  //     }

  //     const operationType = this.operationForm.value.operationType;
  //     // console.log(operationType);
  
  //     if (operationType === 'TRANSFER') {

  //       if (!control.value) {
  //         return { required: true };

  //       } else if (control.value.length < 10) {  
  //         return { minlength: { requiredLength: 10, actualLength: control.value.length } };
  //       }
  //     }
      
  //     return null;
  //   };
  // }

  handleSearchAccount() {
    let accountId : string = this.accountForm.value.accountId;

    if(!accountId || accountId.length <= 10) {
      return null;
    }
    
    this.accountObservable = this.accountService.getAccount(accountId,this.currentPage,this.sizePage).pipe(
      tap((data) => {
        this.accountData = true;
        this.accountIdSource = data.accountId;
        this.Balance = data.balance;
      }),
      catchError( err => {
        if(err.status = 0) {
          alert("Unable to connect to the server. Please check if the database is accessible");
          this.authService.logOut();
        }
        this.accountData = false;
        this.errorMessage = err.error.message;
        return throwError(() => new Error(err));
      })
    );
    
    return this.accountObservable;
  }

  goToPage(page : number){
    this.currentPage = page;
    
    this.handleSearchAccount();
  }

  handleAccountOperations() {

    let accountId : string = this.accountIdSource;

    let operationType = this.operationForm.value.operationType;

    let amount : number = this.operationForm.value.amount;
    let description : string = this.operationForm.value.description;
    let accountDestination : string = this.accountDestinationForm.value.accountDestination;

    if(operationType == "CREDIT") {

      this.accountService.credit(accountId,amount,description).subscribe({
        next : () => {
          alert("Success Credit Operation");
          this.operationForm.reset();
          this.handleSearchAccount();
        },
        error : (err) => {
          this.errorMessage = err.error.message
        }
      })  
       
    }
    else if(operationType == "DEBIT") {

      if(amount <= this.Balance) {
        this.accountService.debit(accountId,amount,description).subscribe({
          next : () => {
            alert("Success Debit Operation");
            this.operationForm.reset();
            this.handleSearchAccount();
          },
          error : (err) => {
            this.errorMessage = err.error.message
          }
        })        
      }
      else {
        alert("Insufficient Balance : "+ this.Balance +" For DEBIT Operation Amount : " + amount);
      }

    }
    else if(operationType == "TRANSFER"){

      if(accountId != accountDestination) {
        if(amount <= this.Balance) {
          this.accountService.transfer(accountId,accountDestination,amount,description).subscribe({
            next : () => {
              alert("Success Transfer Operation");
              this.operationForm.reset();
              this.accountDestinationForm.reset();
              this.handleSearchAccount();
            },
            error : (err) => {
              // console.log(err.error.message);
              this.errorMessage = err.error.message
            }
          })
        }
        else {
          alert("Insufficient Balance : "+ this.Balance +" For TRANSFER Operation Amount : " + amount);
        }
      }
      else {
        alert("You Cannot Transfer To The Same Account, The Account Destination Must Be Different From Your Account.");
      }
    }
  }
}
