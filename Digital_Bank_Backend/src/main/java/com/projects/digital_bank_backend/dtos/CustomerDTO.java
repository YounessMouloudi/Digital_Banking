package com.projects.digital_bank_backend.dtos;

import lombok.Data;
/*  hna f Dto tan7tatjo ghi les attribus li ghaykono f la partie UI front avec les getters et setters safi */
@Data
public class CustomerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String cin;
    private String address;
}
