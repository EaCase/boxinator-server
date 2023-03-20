package com.example.boxinator.auth.client.keycloak;

import com.example.boxinator.auth.client.AuthClient;
import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.dtos.auth.AuthResponse;
import com.example.boxinator.dtos.auth.Credentials;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.account.AccountType;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
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
        try {
            return new RestTemplate().exchange(
                    URL_LOGIN,
                    HttpMethod.POST,
                    builder.buildLoginRequest(credentials),
                    AuthResponse.class
            ).getBody();
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ApplicationException("Invalid user credentials.", HttpStatus.UNAUTHORIZED);
            }
            throw new ApplicationException("Could not log in.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public String register(AuthRegister registrationInfo, AccountType type) {
        AuthResponse serviceAccount = authAsServiceAccount();
        String accessToken = serviceAccount.getAccessToken();

        try {
            var res = new RestTemplate().exchange(
                    URL_REGISTER,
                    HttpMethod.POST,
                    builder.buildRegisterUserRequest(accessToken, registrationInfo, type),
                    JSONObject.class
            );
            return "Account successfully registered.";
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new ApplicationException("The provided email is already in use.", HttpStatus.CONFLICT);
            }
            System.out.println(e);
            e.printStackTrace();
            throw new ApplicationException("Could not register the account with the provided information.", HttpStatus.INTERNAL_SERVER_ERROR);
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
