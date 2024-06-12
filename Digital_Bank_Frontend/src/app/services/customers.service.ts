import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../model/customer.model';
import { environment } from 'src/environments/environment';
import { ValidationErrors } from '@angular/forms';
import { AccountCustomer } from '../model/account.model';


@Injectable({
  providedIn: 'root'
})

export class CustomersService {

  // hna URL ima ndéclariwh haka aw ndécalriwh const f fichier environment ila kona ghan7tajoh f blassa khra 
  // Url_Backend : string = "http://localhost:8080";

  constructor(private http : HttpClient) { }

  public getCustomers() : Observable<Customer[]> {
    return this.http.get<Array<Customer>>(environment.Url_Backend+"/customers");
  }

  public searchCustomers(value : string) : Observable<Customer[]> {
    return this.http.get<Array<Customer>>(environment.Url_Backend+"/customers/search?keyword="+ value)
  }

  public saveCustomer(customer : Customer) : Observable<Customer> {
    return this.http.post<Customer>(environment.Url_Backend+"/customers",customer);
  }

  public deleteCustomer(id : number) : Observable<Customer> {
    return this.http.delete<Customer>(environment.Url_Backend+"/customers/"+ id);
  }

  public updateCustomer(customer : Customer) : Observable<Customer> {
    return this.http.put<Customer>(environment.Url_Backend+"/customers/"+customer.id,customer);
  }

  public getCustomer(id : number) : Observable<Customer> {
    return this.http.get<Customer>(environment.Url_Backend+"/customers/"+ id);
  }

  public getErrorMessage(fieldName : string, error : ValidationErrors){
    if(error["required"]){
      return fieldName + " is required";
    }
    else if (error["minlength"]){
      return fieldName + " should have at least " + error["minlength"]["requiredLength"] + " characters";
    }
    else if (error["maxlength"]){
      return fieldName + " should have at most " + error["maxlength"]["requiredLength"] + " characters";
    }
    else if (error["email"]){
      return fieldName + " should be an email";
    }
    else {
      return "";
    }
  }

  public getCustomerProfile(email : string) : Observable<AccountCustomer[]> {

    // let params = new HttpParams().set("email",email);

    // let options = {
    //   headers : new HttpHeaders().set("Content-Type","application/x-www-form-urlencoded"),
    //   params : params
    // };

    // return this.http.get<AccountCustomer[]>(environment.Url_Backend+"/customerProfile",options);

    return this.http.post<AccountCustomer[]>(environment.Url_Backend+"/customer/profile",email);
  }

  public updatePassword(email:string, oldPassword:string, newPassword:string) {
    let data = {email,oldPassword,newPassword}
    return this.http.post(environment.Url_Backend+"/update/password",data);
  }
}


