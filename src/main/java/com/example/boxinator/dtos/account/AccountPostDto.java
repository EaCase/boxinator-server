package com.example.boxinator.dtos.account;


import lombok.Data;

@Data
public class AccountPostDto {
    private String dateOfBirth;
    private String firstName;
    private String lastName;
    private String zipCode;
    private String contactNumber;
    private Long countryId;
}
