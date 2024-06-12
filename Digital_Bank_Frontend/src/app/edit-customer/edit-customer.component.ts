import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomersService } from '../services/customers.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from '../model/customer.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-edit-customer',
  templateUrl: './edit-customer.component.html',
  styleUrls: ['./edit-customer.component.css']
})
export class EditCustomerComponent implements OnInit {

  editCustomerForm! : FormGroup;
  errorMessage : string = "";
  customerId! : number;
  customer! : Customer;

  constructor(public customerService : CustomersService, private fb : FormBuilder, private route : ActivatedRoute,
    private router : Router,public authService : AuthService) { 
    this.customerId = this.route.snapshot.params['id']; // hadi bach nrécupriw id mn url
  }

  ngOnInit(): void {
    
    this.customerService.getCustomer(this.customerId).subscribe({
      next : (data) => {
        this.customer = data;

        this.editCustomerForm = this.fb.group({
          firstName : this.fb.control(this.customer.firstName, [Validators.required, Validators.minLength(4)]),
          lastName : this.fb.control(this.customer.lastName, [Validators.required, Validators.minLength(4)]),
          email : this.fb.control(this.customer.email, [Validators.required, Validators.email]),
          password : this.fb.control(null, [Validators.minLength(5)]),
          phone : this.fb.control(this.customer.phone, [Validators.required, Validators.minLength(10), Validators.maxLength(10)]),
          cin : this.fb.control(this.customer.cin, Validators.required),
          address : this.fb.control(this.customer.address, Validators.required),
        });

      },
      error : (err) => {
        this.errorMessage = err.error.message;
      }
    })
  }

  handleUpdateCustomer(){
    let c : Customer = this.editCustomerForm.value;
    c.id = this.customer.id;
    
    // console.log(c);

    this.customerService.updateCustomer(c).subscribe({
      next : () => {
        alert("Customer Updated Successfully");
        
        if(this.authService.roles == "USER"){
          this.router.navigateByUrl("/digitalBank/customer-profile");
        }
        else {
          this.router.navigateByUrl("/digitalBank/customers");
        }
      },
      error : (err) => {
        this.errorMessage = err.error.message;
      }
    })
  }

}
