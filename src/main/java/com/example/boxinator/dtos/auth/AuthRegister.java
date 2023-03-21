package com.example.boxinator.dtos.auth;

import lombok.Data;

@Data
public class AuthRegister {
    private String email;
    private String password;
    private String dateOfBirth;
    private String firstName;
    private String lastName;
    private String zipCode;
    private String contactNumber;
    private Long countryId;
}

