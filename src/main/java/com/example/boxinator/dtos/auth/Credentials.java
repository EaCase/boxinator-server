package com.example.boxinator.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credentials {
    /**
     * Should be an email address.
     */
    String username;
    String password;
}
