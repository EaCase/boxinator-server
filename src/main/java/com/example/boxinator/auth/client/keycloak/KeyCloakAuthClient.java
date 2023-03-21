package com.example.boxinator.auth.client.keycloak;

import com.example.boxinator.auth.client.AuthClient;
import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.dtos.auth.AuthResponse;
import com.example.boxinator.dtos.auth.Credentials;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.account.AccountType;
import com.example.boxinator.services.account.AccountService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class KeyCloakAuthClient implements AuthClient {
    @Value("${auth.url.login}")
    private String URL_LOGIN;

    @Value("${auth.url.users}")
    private String URL_USERS;

    private final KeyCloakRequestBuilder builder;
    private final AccountService accountService;

    public KeyCloakAuthClient(KeyCloakRequestBuilder builder, AccountService accountService) {
        this.builder = builder;
        this.accountService = accountService;
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
        String serviceAccountToken = serviceAccount.getAccessToken();

        try {
            var res = new RestTemplate().exchange(
                    URL_USERS,
                    HttpMethod.POST,
                    builder.buildRegisterUserRequest(serviceAccountToken, registrationInfo, type),
                    JSONObject.class
            );

            String userId = Objects.requireNonNull(res.getHeaders().get(HttpHeaders.LOCATION))
                    .get(0).replaceAll(".*/", "");

            // todo assign roles
            accountService.register(registrationInfo, userId);
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

    @Override
    public String delete(String accountId) {
        AuthResponse serviceAccount = authAsServiceAccount();
        String serviceAccountToken = serviceAccount.getAccessToken();
        new RestTemplate().exchange(
                URL_USERS + "/" + accountId,
                HttpMethod.DELETE,
                builder.buildDeleteUserRequest(serviceAccountToken),
                JSONObject.class
        );
        accountService.deleteByProviderId(accountId);
        return accountId;
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
