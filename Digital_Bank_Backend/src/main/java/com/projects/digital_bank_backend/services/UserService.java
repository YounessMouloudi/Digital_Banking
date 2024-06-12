package com.projects.digital_bank_backend.services;

import com.projects.digital_bank_backend.entities.User;
import com.projects.digital_bank_backend.entities.Role;

import java.util.Set;

public interface UserService {

    User addNewUser(String username,String email, String password, Set<Role> role);
    Role addNewRole(String role);

    User getUser(String email);
}
