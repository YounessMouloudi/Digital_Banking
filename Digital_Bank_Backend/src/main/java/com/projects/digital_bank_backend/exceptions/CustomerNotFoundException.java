package com.projects.digital_bank_backend.exceptions;

/* hna hadi tatssma Exception Metier ay drna package khass ghi b les exceptions li ghadi ncréyiwhom 7na
3la 7ssab les exceptions li 3ndna ay matatb9ach me3tamed ghir 3la RuntimeException
*/
public class CustomerNotFoundException extends Exception {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
