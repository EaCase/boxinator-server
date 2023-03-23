package com.example.boxinator.models.account;

import com.example.boxinator.models.country.Country;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
public class Account {
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String providerId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "created_at")
    private LocalDate createdAt;

    private LocalDate dob;

    @Transient
    private AccountType accountType;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "contact_number")
    private String contactNumber;
}
