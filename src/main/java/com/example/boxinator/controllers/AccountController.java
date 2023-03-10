package com.example.boxinator.controllers;

import com.example.boxinator.dtos.account.AccountGetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "account")
public class AccountController {


    @GetMapping("/{id}")
    @Operation(summary = "Get account by id")
    @ApiResponse(
            responseCode = "200",
            description = "Returns a list of accounts",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AccountGetDto.class))
            )}
    )
    public ResponseEntity<List<AccountGetDto>> getAccount(@PathVariable Long id) {
        throw new RuntimeException("Not implemented.");

    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit account by id")
    @ApiResponse(responseCode = "204",
            description = "Accounted edited successfully",
            content = @Content
    )
    public ResponseEntity<AccountGetDto> updateAccount(@PathVariable Long id) {
        throw new RuntimeException("Not implemented.");
    }

    @PostMapping("/")
    @Operation(summary = "Creat an account")
    @ApiResponse(responseCode = "201",
            description = "Account created successfully",
            content =  { @Content(mediaType = "application/json",
                     schema = @Schema(implementation = AccountGetDto.class)
            )}
    )
    public ResponseEntity<AccountGetDto> createAccount() {
        throw new RuntimeException("Not implemented.");
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account")
    @ApiResponse(responseCode = "200",
            description = "Account deleted successfully",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)
            )}
    )
    public ResponseEntity<Long> deleteAccount(@PathVariable Long id) {
        // TODO Admin only
        throw new RuntimeException("Not implemented.");
    }
}
