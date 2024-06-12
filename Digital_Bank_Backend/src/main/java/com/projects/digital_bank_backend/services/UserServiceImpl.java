package com.projects.digital_bank_backend.services;

import com.projects.digital_bank_backend.entities.User;
import com.projects.digital_bank_backend.entities.Role;
import com.projects.digital_bank_backend.repositories.RoleRepository;
import com.projects.digital_bank_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service @Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User addNewUser(String username,String email, String password, Set<Role> roles) {

        User user = userRepository.findByEmail(email);
        if(user != null) throw new RuntimeException("This User Already exists");

        user = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    @Override
    public Role addNewRole(String role) {

        Role userRole = roleRepository.findByRole(role);

        if(userRole != null) throw new RuntimeException("This role Already exists");

        userRole = Role.builder().role(role).build();

//        Role userRole = roleRepository.findById(role)
//                .orElseGet(() -> roleRepository.save(new Role(role)));

//        Role userRole = roleRepository.findById(role)
//                .orElseGet(() -> roleRepository.save(Role.builder().role(role).build()));

        return roleRepository.save(userRole);

    }

    @Override
    public User getUser(String email) {

        User user = userRepository.findByEmail(email);

        return user;
    }

}
