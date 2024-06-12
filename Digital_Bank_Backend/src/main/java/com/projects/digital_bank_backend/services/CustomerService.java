package com.projects.digital_bank_backend.services;

import com.projects.digital_bank_backend.dtos.CustomerDTO;
import com.projects.digital_bank_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {

    /* mhm rah hna f les methodes awal haja 9bal matbda darori khass tdir des régles métiers ay des verifications
       et des tests par ex : tchof wach le client déja existe wach le client f black liste, tester 3la un compte
       wach kayn aw ina type .... ect => had les régles métiers tat7adedhom société
       mais hna ghadi nkhademo normal sans régles
    */

    List<CustomerDTO> listCustomers();
    List<CustomerDTO> searchCustomers(String keyword);

    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException;
    void deleteCustomer(Long id);

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
    String generateRandomPassword();
    CustomerDTO getCustomerProfile(String email) throws CustomerNotFoundException;

    void updatePassword(String email,String oldPassword, String newPassword) throws CustomerNotFoundException;

}
