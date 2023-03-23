package com.example.boxinator.dtos.account;

import lombok.Data;

@Data
public class AccountGetDto {
    private Long id;
    private String email;
    private String accountType;
    private String dob;
    private String firstName;
    private String lastName;
    private Long countryId;
    private String zipCode;
    private String contactNumber;
}
