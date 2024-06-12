import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AccountCustomer, AccountDetails } from '../model/account.model';
import { ValidationErrors } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class AccountsService {

  constructor(private http : HttpClient) { }

  public getAccount(accountID : string, page : number, size : number) : Observable<AccountDetails> {
    return this.http.get<AccountDetails>(environment.Url_Backend+"/accounts/"+ accountID +"/pageOperations?page="
                                        + page +"&size="+ size);
  }

  // credit : الزيادة في الحساب
  public credit(accountId : string, amount : number, description : string) {
    return this.http.post(environment.Url_Backend+"/accounts/credit",{accountId,amount,description});
  }

  // debit : الخصم من الحساب
  public debit(accountId : string, amount : number, description : string) {
    return this.http.post(environment.Url_Backend+"/accounts/debit",{accountId,amount,description});
  }

  // transfer : virement
  public transfer(accountIdSource : string, accountIdDestination : string, amount : number, description : string) {
    let data = {accountIdSource,accountIdDestination,amount,description}
    return this.http.post(environment.Url_Backend+"/accounts/transfer", data);
  }

  public getCustomerAccount(customerId : string) : Observable<AccountCustomer[]>{
    return this.http.get<AccountCustomer[]>(environment.Url_Backend+"/accounts/customer/"+customerId);
  }

  public getErrorMessage(fieldName : string, error : ValidationErrors){
    if(error["required"]){
      return fieldName + " is required";
    }
    else if (error["minlength"]){
      return fieldName + " should have at least " + error["minlength"]["requiredLength"] + " characters";
    }
    else if (error["min"]){
      return fieldName + " should have min value  " + error["min"]["min"];
    }
    else if (error["pattern"]){
      return fieldName + " must be a valid number '" + error["pattern"]["actualValue"] + "' is not a number.";
    }
    else {
      return "";
    }
  }

  public saveCurrentAccount(customerId : number) {
    let data = {
      initialBalance : 100,
      overDraft : 10000,
      customerId : customerId
    }
    return this.http.post(environment.Url_Backend+"/accounts/CurrentBankAccount/"+data.customerId, data);
  }

  // public saveCurrentAccount2(initialBalance : number, overDraft : number, customerId : number) {
  //   let data = {initialBalance, overDraft, customerId}
  //   return this.http.post(environment.Url_Backend+"/accounts/CurrentBankAccount/"+data.customerId,data);
  // }

  public saveSavingAccount(customerId : number) {
    let data = {
      initialBalance : 0, 
      interestRate : 0, 
      customerId : customerId
    }
    return this.http.post(environment.Url_Backend+"/accounts/SavingBankAccount/"+data.customerId,data);
  }

}
