package com.example.boxinator.auth.client.keycloak;


import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.dtos.auth.Credentials;
import com.example.boxinator.models.account.AccountType;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


/**
 * Builds HttpRequests for different Keycloak operations.
 */
@Component
class KeyCloakRequestBuilder {
    @Value("${auth.secret}")
    private String SECRET;

    @Value("${auth.client}")
    private String CLIENT;

    @Value("${auth.role.admin.id}")
    private String ROLE_ID_ADMIN;

    @Value("${auth.role.user.id}")
    private String ROLE_ID_USER;

    public HttpEntity<?> buildLoginRequest(Credentials credentials) {
        return new HttpEntity<>(
                buildLoginBody(credentials),
                buildHeaders(MediaType.APPLICATION_FORM_URLENCODED)
        );
    }

    public HttpEntity<?> buildRefreshRequest(String refreshToken) {
        return new HttpEntity<>(
                buildRefreshTokenBody(refreshToken),
                buildHeaders(MediaType.APPLICATION_FORM_URLENCODED)
        );
    }

    public HttpEntity<?> buildRegisterUserRequest(String serviceAccountAccessToken, AuthRegister credentials, AccountType type) {
        HttpHeaders headers = buildHeaders(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + serviceAccountAccessToken);
        return new HttpEntity<>(
                buildRegisterBody(credentials, type),
                headers
        );
    }

    public HttpEntity<?> buildRoleEditRequest(String serviceAccountAccessToken, AccountType type) {
        HttpHeaders headers = buildHeaders(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + serviceAccountAccessToken);
        return new HttpEntity<>(
                buildEditRoleBody(type),
                headers
        );
    }

    public HttpEntity<?> buildDeleteUserRequest(String serviceAccountAccessToken) {
        HttpHeaders headers = buildHeaders(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + serviceAccountAccessToken);
        return new HttpEntity<>(
                headers
        );
    }

    public HttpEntity<?> buildAuthenticateServiceAccountRequest() {
        return new HttpEntity<>(
                buildServiceAccountLoginBody(),
                buildHeaders(MediaType.APPLICATION_FORM_URLENCODED)
        );
    }

    private JSONArray buildEditRoleBody(AccountType type) {
        var arr = new JSONArray();
        var obj = new JSONObject();

        switch (type) {
            case ADMIN -> {
                obj.put("name", "admin");
                obj.put("id", ROLE_ID_ADMIN);
            }
            case REGISTERED_USER -> {
                obj.put("name", "user");
                obj.put("id", ROLE_ID_USER);
            }
        }
        arr.add(obj);
        return arr;
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


    private JSONObject buildRegisterBody(AuthRegister regInfo, AccountType type) {
        var body = new JSONObject();
        var credentialsArr = new JSONArray();
        var credentialsObject = new JSONObject();
        credentialsObject.put("type", "password");
        credentialsObject.put("value", regInfo.getPassword());
        credentialsObject.put("temporary", false);
        credentialsArr.add(credentialsObject);

        var attributeObject = new JSONObject();
        attributeObject.put("lastName", regInfo.getLastName());
        attributeObject.put("firstName", regInfo.getFirstName());
        attributeObject.put("zipCode", regInfo.getZipCode());
        attributeObject.put("dob", regInfo.getDateOfBirth());
        attributeObject.put("contactNumber", regInfo.getContactNumber());
        attributeObject.put("countryId", regInfo.getCountryId());

        body.put("username", regInfo.getEmail());
        body.put("enabled", true);
        body.put("credentials", credentialsArr);
        body.put("attributes", attributeObject);
        return body;
    }

    private HttpHeaders buildHeaders(MediaType contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType);
        headers.set("Accept", "application/json");
        return headers;
    }
}
