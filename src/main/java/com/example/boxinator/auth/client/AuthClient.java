package com.example.boxinator.auth.client;

import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.dtos.auth.AuthResponse;
import com.example.boxinator.dtos.auth.Credentials;
import com.example.boxinator.models.account.AccountType;
import org.springframework.stereotype.Component;


@Component
public interface AuthClient {
    AuthResponse login(Credentials credentials);

    AuthResponse refresh(String refreshToken);

    /**
     * Register an account.
     *
     * @param registrationInfo account info
     * @param type             account type
     * @return message of a successful registration to send to the client
     */
    String register(AuthRegister registrationInfo, AccountType type);

    /**
     * Delete an account with its providerId.
     *
     * @param accountId the provider issued id for the account.
     */
    void delete(String accountId);
}
