package com.example.boxinator.dtos.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    // In case the email is already used for a temporary account, pass the server generated token with registration data
    // to link the existing temporary account to the full account.
    private String registrationToken;
}

