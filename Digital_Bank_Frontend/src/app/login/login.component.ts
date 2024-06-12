import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  formLogin! : FormGroup;
  errorMessage : string = "";

  constructor(private fb : FormBuilder, public authService : AuthService, private router : Router) { }

  ngOnInit(): void {

    this.formLogin = this.fb.group({
      email : this.fb.control('',[Validators.required,Validators.email]),
      password : this.fb.control("",[Validators.required,Validators.minLength(5)])
    })

  }

  handleLogin() {

    let email = this.formLogin.value.email;
    let psswd = this.formLogin.value.password;

    this.authService.login(email,psswd).subscribe({
      next : (data : any) => {
        // console.log(data["role"]);
        this.authService.loadProfile(data);
        this.router.navigateByUrl('/digitalBank');
      },
      error : err => {

        // Erreur côté serveur
        if (err.status === 0) {
          this.errorMessage = "Unable to connect to the server. Please check if the database is accessible.";
        }
        else {
          this.errorMessage = err.error.message;
        }

        // if(err.error instanceof ErrorEvent){
        //   // Erreur côté client
        //   this.errorMessage = "An error occurred on the client side";         
        // }
      }
    });     
  }
}
