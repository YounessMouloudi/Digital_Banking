package com.projects.digital_bank_backend.repositories;

import com.projects.digital_bank_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {
    User findByEmail(String email);

    @Query("SELECT '*' FROM User u LEFT JOIN Customer c ON u.id = c.id")
    List<User> findAll();
}
