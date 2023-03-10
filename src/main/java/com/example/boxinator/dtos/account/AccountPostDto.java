package com.example.boxinator.dtos.account;


import lombok.Data;

@Data
public class AccountPostDto {
    private String accountEmail;
    private String accountDob;
    private String accountFirstName;
    private String accountLastName;
    private String zipCode;
    private String contactNumber;
    private Long countryId;
}
