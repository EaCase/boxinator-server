package com.example.boxinator.auth.client.keycloak;


import com.example.boxinator.dtos.auth.Credentials;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
class KeyCloakRequestBuilder {
    @Value("${auth.secret}")
    private String SECRET;

    @Value("${auth.client}")
    private String CLIENT;

    public HttpEntity<MultiValueMap<String, String>> buildLoginRequest(Credentials credentials) {
        return new HttpEntity<>(
                buildLoginBody(credentials),
                buildHeaders(MediaType.APPLICATION_FORM_URLENCODED)
        );
    }

    public HttpEntity<MultiValueMap<String, String>> buildRefreshRequest(String refreshToken) {
        return new HttpEntity<>(
                buildRefreshTokenBody(refreshToken),
                buildHeaders(MediaType.APPLICATION_FORM_URLENCODED)
        );
    }

    public HttpEntity<JSONObject> buildRegisterUserRequest(String accessToken, Credentials credentials) {
        HttpHeaders headers = buildHeaders(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(
                buildRegisterBody(credentials),
                headers
        );
    }

    public HttpEntity<MultiValueMap<String, String>> buildAuthenticateServiceAccountRequest() {
        return new HttpEntity<>(
                buildServiceAccountLoginBody(),
                buildHeaders(MediaType.APPLICATION_FORM_URLENCODED)
        );
    }

    private MultiValueMap<String, String> buildLoginBody(Credentials credentials) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", credentials.getUsername());
        body.add("password", credentials.getPassword());
        body.add("client_secret", SECRET);
        body.add("client_id", CLIENT);
        body.add("grant_type", "password");
        return body;
    }

    private MultiValueMap<String, String> buildRefreshTokenBody(String token) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_secret", SECRET);
        body.add("client_id", CLIENT);
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", token);
        return body;
    }

    private MultiValueMap<String, String> buildServiceAccountLoginBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_secret", SECRET);
        body.add("client_id", CLIENT);
        body.add("grant_type", "client_credentials");
        return body;
    }

    private JSONObject buildRegisterBody(Credentials credentials) {
        var body = new JSONObject();
        var credentialsArr = new JSONArray();
        var credentialsObject = new JSONObject();
        credentialsObject.put("type", "password");
        credentialsObject.put("value", credentials.getPassword());
        credentialsObject.put("temporary", false);
        credentialsArr.add(credentialsObject);
        body.put("email", credentials.getUsername());
        body.put("enabled", true);
        body.put("credentials", credentialsArr);
        return body;
    }

    private HttpHeaders buildHeaders(MediaType contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType);
        headers.set("Accept", "application/json");
        return headers;
    }
}
