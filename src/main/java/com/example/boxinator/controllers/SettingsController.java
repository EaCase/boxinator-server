package com.example.boxinator.controllers;

import com.example.boxinator.dtos.country.CountryGetDto;
import com.example.boxinator.dtos.country.CountryPostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "settings")
public class SettingsController {

    @GetMapping("/countries")
    @Operation(summary = "Get all the countries.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns a list of the country objects.",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CountryGetDto.class))
            )}
    )
    public ResponseEntity<List<CountryGetDto>> getCountries() {
        throw new RuntimeException("Not implemented.");
    }

    @PostMapping("/countries")
    @Operation(summary = "Add a new country by providing its name.")
    @ApiResponse(
            responseCode = "201",
            description = "Returns the created country.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CountryGetDto.class)
            )}
    )
    public ResponseEntity<CountryGetDto> addCountry(@RequestBody CountryPostDto body) {
        throw new RuntimeException("Not implemented.");
    }

    @PutMapping("/countries/{id}")
    @Operation(summary = "Edit the name of a country.")
    @ApiResponse(
            responseCode = "204",
            description = "Returns the country with the new name.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CountryGetDto.class)
            )}
    )
    public ResponseEntity<CountryGetDto> updateCountry(@PathVariable Long id, @RequestBody CountryPostDto body) {
        throw new RuntimeException("Not implemented.");
    }
}
