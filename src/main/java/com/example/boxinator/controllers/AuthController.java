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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
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
    @Operation(summary = "Log in a user.")
    @ApiResponse(
            responseCode = "200",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AuthResponse.class))
            )}
    )
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
    @Operation(summary = "Register a user with an email address and a password. In a case where a temporary account " +
            "is being fully registered, the 'token' value should be passed in request params. In this case, the email address in request body " +
            "must match the one where the token was sent to.")
    @ApiResponse(
            responseCode = "200",
            description = "String message of a successful registration.",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AuthResponse.class))
            )}
    )
    public ResponseEntity<String> register(@RequestBody AuthRegister registrationInfo, @RequestParam(required = false) String token) {
        registrationInfo.setRegistrationToken(token);
        var result = authClient.register(registrationInfo, AccountType.REGISTERED_USER);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh the session of a user by providing a refreshToken in the request body.")
    @ApiResponse(
            responseCode = "200",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AuthResponse.class))
            )}
    )
    public ResponseEntity<AuthResponse> refresh(@RequestBody AuthRefresh body) {
        var result = authClient.refresh(body.getRefreshToken());
        return ResponseEntity.ok().body(result);
    }
}
