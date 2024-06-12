import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CustomersService } from '../services/customers.service';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css']
})
export class UpdatePasswordComponent implements OnInit {

  formPassword! : FormGroup;
  errorMessage : string = "";
  
  constructor(private fb : FormBuilder, public customerService : CustomersService, public authService : AuthService,
    private router : Router) { }

  ngOnInit(): void {
    
    if(this.authService.roles == "USER") {

      this.formPassword = this.fb.group({
        oldPassword : this.fb.control("",[Validators.required,Validators.minLength(5)]),
        newPassword : this.fb.control("",[Validators.required,Validators.minLength(5)]),
        confirmPassword : this.fb.control("",[Validators.required,Validators.minLength(5)]),
      });
    }
    else {
      alert("You Are Not Authorized To Access This Page !!!");
      this.router.navigateByUrl('/digitalBank');
    }
  }

  handleUpdate() {
    
    let email = this.authService.email;
    let oldPassword = this.formPassword.value.oldPassword;
    let newPassword = this.formPassword.value.newPassword;
    let confirmPassword = this.formPassword.value.confirmPassword;

    if(confirmPassword == newPassword) {
      this.customerService.updatePassword(email,oldPassword,newPassword).subscribe({
        next : () => {
          // console.log(data);
          alert("Password Updated Successfully");
          this.router.navigateByUrl('/digitalBank/customer-profile');
        },
        error : err => {
          // console.log(err);
          this.errorMessage = err.error.message;
        }
      })
    }
    else{
      this.errorMessage = "Confirmation Password Doesn't Match The New Password.";
    }
    
  }

}
