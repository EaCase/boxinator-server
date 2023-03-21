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

    String register(AuthRegister registrationInfo, AccountType type);

    String delete(String accountId);
}
