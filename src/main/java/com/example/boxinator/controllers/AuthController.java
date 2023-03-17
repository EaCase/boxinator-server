package com.example.boxinator.controllers;

import com.example.boxinator.auth.client.AuthClient;
import com.example.boxinator.dtos.auth.AuthRefresh;
import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.dtos.auth.Credentials;
import com.example.boxinator.dtos.auth.AuthResponse;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.account.AccountType;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping(value = "auth")
public class AuthController {

    //    private final Bucket bucket;
    private final AuthClient authClient;

    public AuthController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Credentials credentials) {
        /*
            Authenticates a user. Accepts appropriate parameters in the request body as application/json.
            This should also return their account type (customer, or administrator – See Appendix A).
            You will need this to determine what is or is not displayed on your front end. Make use
            of best practices and use a Basic Authentication token in the HTTP Header.

            A failed authentication attempt should prompt a 401 Unauthorized response. This
            endpoint should also be subject to a rate limiting policy where if authentication is
            attempted too many times (unsuccessfully) then requests from the corresponding address
            should be temporarily ignored. Candidates should decide on an appropriate threshold
            for rate limiting.
        */
//        if (!bucket.tryConsume(1)) {
//            throw new ApplicationException("Too many requests.", HttpStatus.TOO_MANY_REQUESTS);
//        }
        var result = authClient.login(credentials);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRegister registrationInfo) {
        var result = authClient.register(registrationInfo, AccountType.REGISTERED_USER);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody AuthRefresh body) {
        var result = authClient.refresh(body.getRefreshToken());
        return ResponseEntity.ok().body(result);
    }
}
