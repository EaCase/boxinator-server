package com.example.boxinator.auth.client;

import com.example.boxinator.dtos.auth.AuthResponse;
import com.example.boxinator.dtos.auth.Credentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KeyCloakAuthClient implements AuthClient {
    private static String GRANT_TYPE = "password";

    @Value("${auth.url}")
    private static String URL;

    @Value("${auth.secret}")
    private static String SECRET;

    @Value("${auth.client}")
    private static String CLIENT;


    @Override
    public ResponseEntity<AuthResponse> login(Credentials credentials) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(
                buildLoginBody(credentials),
                buildHeaders()
        );
        return restTemplate.exchange(URL, HttpMethod.POST, request, AuthResponse.class);
    }

    @Override
    public ResponseEntity<AuthResponse> register(Credentials credentials) {
        return null;
    }

    private MultiValueMap<String, String> buildLoginBody(Credentials credentials) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", credentials.getUsername());
        body.add("password", credentials.getPassword());
        body.add("client_secret", SECRET);
        body.add("client_id", CLIENT);
        body.add("grant_type", GRANT_TYPE);
        return body;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", "application/json");
        return headers;
    }
}
