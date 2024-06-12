package com.projects.digital_bank_backend.repositories;

import com.projects.digital_bank_backend.dtos.CustomerDTO;
import com.projects.digital_bank_backend.entities.BankAccount;
import com.projects.digital_bank_backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {

    // hna dart 2 methodes pour récupérer les bankAccounts by customerId par query et par repository qui est par défault
    // @Query("select ba from BankAccount ba where ba.customer.id = :customerId")
    // List<BankAccount> findAllByCustomerIdOOrderBy(@Param("customerId") Long customerId);
    List<BankAccount> findBankAccountsByCustomer_Id(Long customerId);

}
