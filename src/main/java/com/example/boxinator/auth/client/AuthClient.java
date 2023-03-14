package com.example.boxinator.auth.client;

import com.example.boxinator.dtos.auth.AuthResponse;
import com.example.boxinator.dtos.auth.Credentials;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface AuthClient {
    ResponseEntity<AuthResponse> login(Credentials credentials);

    ResponseEntity<AuthResponse> register(Credentials credentials);
}
