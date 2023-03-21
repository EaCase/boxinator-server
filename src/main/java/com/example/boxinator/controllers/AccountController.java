package com.example.boxinator.controllers;

import com.example.boxinator.auth.client.AuthClient;
import com.example.boxinator.dtos.account.AccountGetDto;
import com.example.boxinator.dtos.account.AccountMapper;
import com.example.boxinator.dtos.account.AccountPostDto;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.account.AccountType;
import com.example.boxinator.services.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "account")
public class AccountController {
    private final AccountService accountService;
    private final AuthClient authClient;
    private final AccountMapper mapper;

    public AccountController(@Qualifier("accountServiceImpl") AccountService accService, AuthClient authClient, AccountMapper mapper) {
        this.accountService = accService;
        this.authClient = authClient;
        this.mapper = mapper;
    }

    @GetMapping("/")
    @Operation(summary = "Get logged in account information.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns account information.",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AccountGetDto.class))
            )}
    )
    public ResponseEntity<AccountGetDto> getAccount(Authentication auth) {
        var jwt = (Jwt) auth.getPrincipal();
        Account account = accountService.getByProviderId(jwt.getSubject());
        return buildResponse(auth, account);
    }

    @PutMapping("/")
    @Operation(summary = "Edit account details.")
    @ApiResponse(responseCode = "204",
            description = "Accounted edited successfully",
            content = @Content
    )
    public ResponseEntity<AccountGetDto> updateAccount(Authentication auth, @RequestBody AccountPostDto dto) {
        var jwt = (Jwt) auth.getPrincipal();
        Account account = accountService.edit(dto, jwt.getSubject());
        return buildResponse(auth, account);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account")
    @ApiResponse(responseCode = "200",
            description = "Account deleted successfully",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)
            )}
    )
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        Account account = accountService.getById(id);
        authClient.delete(account.getProviderId());
        return ResponseEntity.ok().body("Deleted.");
    }


    private ResponseEntity<AccountGetDto> buildResponse(Authentication auth, Account account) {
        AccountType type = AccountType.getAccountType(auth.getAuthorities());
        account.setAccountType(type);
        return ResponseEntity.ok().body(mapper.toDto(account));
    }
}
