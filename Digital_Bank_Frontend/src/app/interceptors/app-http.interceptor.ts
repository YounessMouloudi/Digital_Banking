import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

/* hna darna had interceptor bach mnin nabghiw n accediw l ayi request nssefto m3ah token ila madarnach 
  haka ghadi ykhassna f kol methode f service nsefto m3ah had token f l headers alors hadi ahssan tari9a
  w il a masseftnach had token ga3 ghanl9aw mochkil f backend maghadich ykhalina n accediw les données 
  
  bach ngénériw had interceptor tandiro : ng g interceptor nomInterceptor w darori khass déclarih f app.modules
*/

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {

  constructor(private authService : AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    
    // console.log(request.url);

    /* hna darna had if bach maysseftch lina had authorization ta f login hit login makhassoch token ay ta 
    yt authentifia 3ad f ayi request tlabnaha khass ykon fiha token sinon maykhalihch yadkhol liha */
    if(!request.url.includes("/auth/login")) {
      let newRequest = request.clone({
        headers : request.headers.set("Authorization","Bearer "+ this.authService.accessToken)
      })
      
      return next.handle(newRequest).pipe(
        catchError( err => {
          // console.log(err);
          if(err.status == 401){
            this.authService.logOut();
            return throwError(() => new Error(err));            
          }
          else if(err.status == 0){
            this.authService.logOut();
          }
          // return throwError(() => new Error(err));
          return throwError(err);
        })
      ); 
    }
    else {
      return next.handle(request); 
    }
  }
}
