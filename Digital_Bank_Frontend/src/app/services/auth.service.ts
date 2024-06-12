import { Location } from '@angular/common';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAuthenticated : boolean = false;
  roles : any;
  accessToken! : string;
  username! : string
  email! : string;

  constructor(private http : HttpClient, private router : Router, private location: Location) { }

  public login(email : string , password : string) {
        
    let params = new HttpParams().set("email",email).set("password",password);

    let options = {
      headers : new HttpHeaders().set("Content-Type","application/x-www-form-urlencoded"),
    };
    
    return this.http.post(environment.Url_Backend+"/auth/login", params, options)
  }

  public loadProfile(data : any){
    // console.log(data);
    this.accessToken = data["access-token"];
    this.isAuthenticated = true;
    let jwtDecoded: any = jwtDecode(this.accessToken);
    // console.log(jwtDecoded)
    this.username = jwtDecoded.sub;
    this.email = jwtDecoded.email;
    this.roles = jwtDecoded.scope;
    localStorage.setItem("jwt-token",this.accessToken);
  }

  public logOut() {
    this.isAuthenticated = false;
    this.accessToken = "";
    this.roles = undefined;
    this.username = "";
    this.email = "";
    localStorage.removeItem("jwt-token");
    this.router.navigateByUrl("/login");
  }

  public loadJwtTokenFromlocStorage() {
    const token = localStorage.getItem("jwt-token");
    // console.log(token);
    if(token){
      this.loadProfile({"access-token" : token});
      
      if(this.location.path() == "/login" || this.location.path() == ""){
        this.router.navigateByUrl("/digitalBank");
      }
    }
    else {
      this.logOut();
    }
  }

  public getErrorMessage(fieldName : string, error : ValidationErrors){
    if(error["required"]){
      return fieldName + " is required";
    }
    else if (error["minlength"]){
      return fieldName + " should have at least " + error["minlength"]["requiredLength"] + " characters";
    }
    else if (error["email"]){
      return fieldName + " should be an email";
    }
    else {
      return "";
    }
  }

}
