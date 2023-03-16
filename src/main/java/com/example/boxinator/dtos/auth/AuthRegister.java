package com.example.boxinator.dtos.auth;

import lombok.Data;

@Data
public class AuthRegister {
    String email;
    String password;
    String dateOfBirth;
    String firstName;
    String lastName;
    String zipCode;
    String contactNumber;
    String countryId;
}

