import { Component, OnInit } from '@angular/core';
import { Customer } from '../model/customer.model';
import { Observable, catchError, throwError } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CustomersService } from '../services/customers.service';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {

  // customers1! : Array<Customer>;
  customers! : Observable<Customer[]>;
  errorMessage : string = "";
  searchForm! : FormGroup;

  constructor(private customerService : CustomersService, private fb : FormBuilder,
    private router : Router, public authService : AuthService) { }

  ngOnInit(): void {

    this.searchForm = this.fb.group({
      keyword : this.fb.control("")
    })


    // hna 3ndna 2 methode bach n affichiw les données li jbna mn api 
    
    /* metho 1 : hna khdmna normal b subscribe w jbdna les données w 7tinahom f customers1 li de type liste
     or any */
    // this.customerService.getCustomers().subscribe({
    //   next : (data) => {
    //     this.customers1 = data;
    //   },
    //   error : (err) => {
    //     this.errorMessage =  err.message;
    //   }
    // })

    /* metho 2 : hna customers ghatkon de type Observable w maghadich nkhadmo b subscribe walakin bach n 
    affichiw les données f view darori khassna nsta3mlo l'attribut async f la boucle for */
    // this.customers = this.customerService.getCustomers().pipe(
    //   catchError( err => {
    //     this.errorMessage = err.message;
    //     return throwError(() => new Error(err));
    //     // return throwError(err); // hadi li kona tandiro mais db deprecated alors bdelnaha b hadi li lfo9
    //   })
    // );

    /* hna bach n minimisiw lcode ghadi n3ayto direct 3la l fonction d search alors ghadi ncommentiw metho 2
    hit tadir nafss l9adia ay ila kan search khawi ghatakhod chaine vide w ghatjib lina ga3 les customers 
    w ila kan 3amr ghat searchi
    */
    this.handleSearchCustomers();
  }

  handleSearchCustomers(){
    let keyW = this.searchForm?.value.keyword; 

    this.customers = this.customerService.searchCustomers(keyW).pipe(
      catchError( err => {
        this.errorMessage = err.error.message;
        return throwError(() => new Error(err));
      })
    );
  }

  handleDeleteCustomer(id : number) {
    let confirm = window.confirm("Are you sure you want to delete this customer ?");
    
    if(confirm) {
      this.customerService.deleteCustomer(id).subscribe({
        next : () => {
          alert("Customer is deleted SuccessFully");
          this.handleSearchCustomers();
        },
        error : (err) => {
          this.errorMessage = err.error.message;
        }
      })   
    }
  }

  handleEditCustomer(id : number) {
    this.router.navigateByUrl("/digitalBank/edit/customer/"+id);
  }

  /* hna 3ndna 2 solutions ima nkhadmo b id direct b7al hadi li lta7et aw nkhadmo b obj customer w li ghadi 
  nseftoh f state labghina n affichiw ta les données dial customer w hadi dial state hssan hit mnin ta trefrecher
  l url matayb9ach ybayan lik les données khassk ta tkon jay direct mn lbutton machi b7al ila derti "id" wakha
  trefrecher tayb9aw les données ybano

  handleCustomerAccount(id: number) {
    this.router.navigateByUrl("/customer-account/"+id);
  }
  */
 handleCustomerAccount(customer: Customer) {
   this.router.navigateByUrl("/digitalBank/customer-account/"+customer.id,{state : customer});
 }

}
