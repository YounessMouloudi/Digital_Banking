package com.projects.digital_bank_backend.repositories;

import com.projects.digital_bank_backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @Query("select c from Customer c where c.firstName LIKE %:kw% or c.lastName LIKE %:kw% or c.cin LIKE %:kw% or :kw is NULL")
    List<Customer> searchCustomer(@Param("kw") String keyword);
    // kan dayr hadi f lewal mais galik ba3d lmerat matatkhdemach mzn dackhi bach dar query
    // List<Customer> findByNameContains(String keyword);

    Customer findByEmail(String email);
    Customer findByCin(String CIN);

}
