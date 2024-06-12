package com.projects.digital_bank_backend.repositories;

import com.projects.digital_bank_backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
    Role findByRole(String name);
}
