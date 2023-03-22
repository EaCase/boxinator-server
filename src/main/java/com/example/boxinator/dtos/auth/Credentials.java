package com.example.boxinator.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
    /**
     * Should be an email address.
     */
    String username;
    String password;
}
