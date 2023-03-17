package com.example.boxinator.dtos.auth;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    @JsonAlias("access_token")
    String accessToken;

    @JsonAlias("expires_in")
    String expiresIn;

    @JsonAlias("refresh_expires_in")
    String refreshExpiresIn;

    @JsonAlias("refresh_token")
    String refreshToken;

    @JsonAlias("token_type")
    String tokenType;

    @JsonAlias("session_state")
    String sessionState;

    @JsonAlias("not-before-policy")
    String notBeforePolicy;

    String scope;

    String accountType;
}