package com.example.boxinator.auth.client.keycloak;

import com.example.boxinator.auth.client.AuthClient;
import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.dtos.auth.AuthResponse;
import com.example.boxinator.dtos.auth.Credentials;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.account.AccountType;
import com.example.boxinator.services.account.AccountService;
import com.nimbusds.jose.shaded.gson.Gson;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Objects;

/**
 * Handles authentication and account creation via Keycloak.
 */
@Component
public class KeyCloakAuthClient implements AuthClient {
    @Value("${auth.url.login}")
    private String URL_LOGIN;

    @Value("${auth.url.users}")
    private String URL_USERS;

    @Value("${auth.client.id}")
    private String KEYCLOAK_CLIENT_ID;

    @Value("${auth.client}")
    private String KEYCLOAK_CLIENT;

    private final KeyCloakRequestBuilder builder;
    private final AccountService accountService;

    public KeyCloakAuthClient(KeyCloakRequestBuilder builder, AccountService accountService) {
        this.builder = builder;
        this.accountService = accountService;
    }

    @Override
    public AuthResponse login(Credentials credentials) {
        try {
            return withAccountType(Objects.requireNonNull(new RestTemplate().exchange(
                    URL_LOGIN,
                    HttpMethod.POST,
                    builder.buildLoginRequest(credentials),
                    AuthResponse.class
            ).getBody()));
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ApplicationException("Invalid user credentials.", HttpStatus.UNAUTHORIZED);
            }
            throw new ApplicationException("Could not log in.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        return withAccountType(Objects.requireNonNull(new RestTemplate().exchange(
                URL_LOGIN,
                HttpMethod.POST,
                builder.buildRefreshRequest(refreshToken),
                AuthResponse.class
        ).getBody()));
    }


    @Override
    public String register(AuthRegister registrationInfo, AccountType type) {
        AuthResponse serviceAccount = authAsServiceAccount();
        String serviceAccountToken = serviceAccount.getAccessToken();

        // Sanity check, if token is provided, the email needs to match the one in db under the token value
        if (registrationInfo.getRegistrationToken() != null) {
            var ok = accountService.emailMatchesToken(registrationInfo.getEmail(), registrationInfo.getRegistrationToken());
            if (!ok) {
                throw new ApplicationException("The provided email needs to match the email for the token.", HttpStatus.BAD_REQUEST);
            }
        }

        try {
            var res = new RestTemplate().exchange(
                    URL_USERS,
                    HttpMethod.POST,
                    builder.buildRegisterUserRequest(serviceAccountToken, registrationInfo, type),
                    JSONObject.class
            );

            String userId = Objects.requireNonNull(res.getHeaders().get(HttpHeaders.LOCATION))
                    .get(0).replaceAll(".*/", "");

            // Try catch the other requests, if something goes wrong here, rollback the
            // account creation by deleting the account from keycloak.
            try {
                var roleResponse = new RestTemplate().exchange(
                        URL_USERS + "/" + userId + "/role-mappings/clients/" + KEYCLOAK_CLIENT_ID,
                        HttpMethod.POST,
                        builder.buildRoleEditRequest(serviceAccountToken, type),
                        JSONObject.class
                );

                if (roleResponse.getStatusCode() != HttpStatus.NO_CONTENT) {
                    System.err.println("Failed to set user roles.");
                    throw new RuntimeException("Something went wrong.");
                }
                accountService.register(registrationInfo, userId);
                return "Account successfully registered.";
            } catch (Exception e) {
                this.delete(userId);
                e.printStackTrace();
                throw new ApplicationException("Something went wrong during the registration process.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (RestClientResponseException e) {
            e.printStackTrace();
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new ApplicationException("The provided email is already in use.", HttpStatus.CONFLICT);
            }
            throw new ApplicationException("Could not register the account with the provided information.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public void delete(String accountId) {
        AuthResponse serviceAccount = authAsServiceAccount();
        String serviceAccountToken = serviceAccount.getAccessToken();
        new RestTemplate().exchange(
                URL_USERS + "/" + accountId,
                HttpMethod.DELETE,
                builder.buildDeleteUserRequest(serviceAccountToken),
                JSONObject.class
        );
        accountService.deleteByProviderId(accountId);
    }

    private AuthResponse authAsServiceAccount() {
        return new RestTemplate().exchange(
                URL_LOGIN,
                HttpMethod.POST,
                builder.buildAuthenticateServiceAccountRequest(),
                AuthResponse.class
        ).getBody();
    }


    private AuthResponse withAccountType(AuthResponse response) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = response.getAccessToken().split("\\.");
        var g = new Gson();
        JSONObject payload = g.fromJson(new String(decoder.decode(chunks[1])), JSONObject.class);
        var resAccess = g.fromJson(payload.getAsString("resource_access"), JSONObject.class);
        var client = g.fromJson(resAccess.getAsString(KEYCLOAK_CLIENT), JSONObject.class);
        var roles = g.fromJson(client.getAsString("roles"), JSONArray.class);

        if (roles.contains("admin")) {
            response.setAccountType(AccountType.ADMIN);
        } else if (roles.contains("user")) {
            response.setAccountType(AccountType.REGISTERED_USER);
        }
        return response;
    }
}
