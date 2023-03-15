package com.example.boxinator.auth.client.keycloak;

import com.example.boxinator.auth.client.AuthClient;
import com.example.boxinator.dtos.auth.AuthResponse;
import com.example.boxinator.dtos.auth.Credentials;
import com.example.boxinator.errors.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class KeyCloakAuthClient implements AuthClient {
    @Value("${auth.url.login}")
    private String URL_LOGIN;

    @Value("${auth.url.register}")
    private String URL_REGISTER;

    private final KeyCloakRequestBuilder builder;

    public KeyCloakAuthClient(KeyCloakRequestBuilder builder) {
        this.builder = builder;
    }

    @Override
    public AuthResponse login(Credentials credentials) {
        return new RestTemplate().exchange(
                URL_LOGIN,
                HttpMethod.POST,
                builder.buildLoginRequest(credentials),
                AuthResponse.class
        ).getBody();
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        return new RestTemplate().exchange(
                URL_LOGIN,
                HttpMethod.POST,
                builder.buildRefreshRequest(refreshToken),
                AuthResponse.class
        ).getBody();
    }


    @Override
    public String register(Credentials credentials) {
        AuthResponse serviceAccount = authAsServiceAccount();
        String accessToken = serviceAccount.getAccessToken();
        try {
            new RestTemplate().exchange(
                    URL_REGISTER,
                    HttpMethod.POST,
                    builder.buildRegisterUserRequest(accessToken, credentials),
                    AuthResponse.class
            );
            return "Account successfully registered.";
        } catch (RestClientException e) {
            throw new ApplicationException("Could not register an account with the provided information.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private AuthResponse authAsServiceAccount() {
        return new RestTemplate().exchange(
                URL_LOGIN,
                HttpMethod.POST,
                builder.buildAuthenticateServiceAccountRequest(),
                AuthResponse.class
        ).getBody();
    }
}
