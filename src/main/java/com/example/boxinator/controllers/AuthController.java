package com.example.boxinator.controllers;

import com.example.boxinator.auth.client.AuthClient;
import com.example.boxinator.dtos.auth.AuthRefresh;
import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.dtos.auth.Credentials;
import com.example.boxinator.dtos.auth.AuthResponse;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.account.AccountType;
import com.example.boxinator.ratelimiter.IpRateLimiter;
import com.example.boxinator.ratelimiter.RateLimiterService;
import com.example.boxinator.utils.HttpUtils;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "auth")
public class AuthController {
    private static final long SECOND_NANO = 1_000_000_000;
    private final RateLimiterService rateLimiterService;
    private final AuthClient authClient;

    public AuthController(AuthClient authClient, IpRateLimiter rateLimiterService) {
        this.authClient = authClient;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Credentials credentials, HttpServletRequest request) {
        Bucket bucket = rateLimiterService.resolveBucket(HttpUtils.getRequestIP(request));
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            var result = authClient.login(credentials);
            return ResponseEntity.ok().body(result);
        }

        long refillTime = probe.getNanosToWaitForRefill() / SECOND_NANO;
        throw new ApplicationException("Too many requests. Retry after " + refillTime + " seconds.", HttpStatus.TOO_MANY_REQUESTS);
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
